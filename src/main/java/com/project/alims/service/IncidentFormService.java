package com.project.alims.service;

import com.project.alims.model.IncidentForm;
import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.repository.IncidentFormRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentFormService {

    private final IncidentFormRepository incidentFormRepository;
    private final MaterialRepository materialRepository;
    private final InventoryLogService inventoryLogService;

    @Autowired
    public IncidentFormService(IncidentFormRepository incidentFormRepository, MaterialRepository materialRepository, InventoryLogService inventoryLogService) {
        this.incidentFormRepository = incidentFormRepository;
        this.materialRepository = materialRepository;
        this.inventoryLogService = inventoryLogService;
    }

    public void createInventoryLog(Material existingIncidentMaterial, IncidentForm incidentForm, Integer deductedAmount, Long[] users) {
        if (existingIncidentMaterial != null) {
            InventoryLog inventoryLog = new InventoryLog(
                    users[0],
                    existingIncidentMaterial.getMaterialId(),
                    LocalDate.now(),
                    -deductedAmount,
                    "IncidentForm: " + incidentForm.getIncidentFormId(),
                    "Incident: " + deductedAmount + " " + existingIncidentMaterial.getUnit() + " of " +
                            existingIncidentMaterial.getItemName() + " on " + incidentForm.getDate() + " by Users " +
                            Arrays.toString(users)
            );
            inventoryLogService.createInventoryLog(inventoryLog);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public void deductQuantitytoMaterial(IncidentForm incidentForm, Integer[] quantityDeductions, Long[] materialIds, Long[] userIds) {
        for(int i=0; i < materialIds.length; i++) {
            Material existingMaterial = materialRepository.findById(materialIds[i])
                    .orElseThrow(() -> new RuntimeException("Material not found with ID"));
            if(quantityDeductions[i] != 0) createInventoryLog(existingMaterial, incidentForm, quantityDeductions[i], userIds);
        }
    }

    public IncidentForm saveIncidentForm(IncidentForm incidentForm, MultipartFile file) throws IOException {

        if (file.getSize() > 0) {
            incidentForm.setFile(file.getBytes());
            incidentForm.setAttachments(file.getOriginalFilename());
            incidentForm.setFileType(file.getContentType());
        }

        String[] quantities = incidentForm.getQty().split(",");
        String[] materials = incidentForm.getMaterialId().split(",");
        String[] users = incidentForm.getUserId().split(",");

        if (quantities.length != materials.length) {
            throw new RuntimeException("Quantity amount do not match with Material amount");
        }

        Integer[] materialQuantities = Arrays.stream(quantities)
                .map(Integer::parseInt) // Convert each string to Integer
                .toArray(Integer[]::new);
        Long[] materialIds = Arrays.stream(materials)
                .map(Long::parseLong) // Convert each string to Long
                .toArray(Long[]::new);
        Long[] userIds = Arrays.stream(users)
                .map(Long::parseLong) // Convert each string to Long
                .toArray(Long[]::new);

        IncidentForm createdIncidentForm = incidentFormRepository.save(incidentForm);
        deductQuantitytoMaterial(incidentForm, materialQuantities, materialIds, userIds);

        return createdIncidentForm;
    }

    public List<IncidentForm> getAllIncidentForms() {
        return incidentFormRepository.findAll();
    }

    public Optional<IncidentForm> getIncidentFormById(Long id) {
        return incidentFormRepository.findById(id);
    }

    public IncidentForm updateIncidentForm(Long id, IncidentForm updatedIncidentForm, MultipartFile file) throws IOException {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));

        String users = existingIncidentForm.getUserId();

        // only things that matter to deduct, therefore updated first
        // others are below to avoid problems with calculation
        if(updatedIncidentForm.getDate() != null) existingIncidentForm.setDate(updatedIncidentForm.getDate());
        if(updatedIncidentForm.getUserId() != null) {
            existingIncidentForm.setUserId(updatedIncidentForm.getUserId());
            users = updatedIncidentForm.getUserId();
        }

        String[] userArray = users.split(",");
        Long[] userIds = Arrays.stream(userArray)
                .map(Long::parseLong) // Convert each string to Long
                .toArray(Long[]::new);

        if(updatedIncidentForm.getQty() != null || updatedIncidentForm.getMaterialId() != null) {
            String previousQuantities = existingIncidentForm.getQty();
            String previousMaterials = existingIncidentForm.getMaterialId();
            String newQuantities = existingIncidentForm.getQty();
            String newMaterials = existingIncidentForm.getMaterialId();

            if(updatedIncidentForm.getMaterialId() != null) newMaterials = updatedIncidentForm.getMaterialId();
            if(updatedIncidentForm.getQty() != null) newQuantities = updatedIncidentForm.getQty();

            String[] prevQuantityArray = previousQuantities.split(",");
            String[] prevMaterialArray = previousMaterials.split(",");
            String[] newQuantityArray = newQuantities.split(",");
            String[] newMaterialArray = newMaterials.split(",");

            if (newQuantityArray.length != newMaterialArray.length) {
                throw new RuntimeException("Quantity amount do not match with Material amount");
            }

            Integer[] prevMaterialQuantities = Arrays.stream(prevQuantityArray)
                    .map(Integer::parseInt) // Convert each string to Integer
                    .toArray(Integer[]::new);
            Long[] prevMaterialIds = Arrays.stream(prevMaterialArray)
                    .map(Long::parseLong) // Convert each string to Long
                    .toArray(Long[]::new);

            Integer[] newMaterialQuantities = Arrays.stream(newQuantityArray)
                    .map(Integer::parseInt) // Convert each string to Integer
                    .toArray(Integer[]::new);
            Long[] newMaterialIds = Arrays.stream(newMaterialArray)
                    .map(Long::parseLong) // Convert each string to Long
                    .toArray(Long[]::new);

            Integer[] quantityDeductions = null;
            Long[] materialIds = null;
            if (updatedIncidentForm.getMaterialId() != null) {
                // only materials list updated
                // check for not present in new materials list -> quantity should be negative of previous amount (add back)
                ArrayList<Long> tempMaterials = new ArrayList<Long>(Arrays.asList(newMaterialIds)); // temp
                ArrayList<Long> prevMaterials = new ArrayList<Long>(Arrays.asList(prevMaterialIds)); // just for contains
                ArrayList<Integer> tempQuantities = new ArrayList<Integer>(); ; // temp

                // assume same length - therefore only involves reordering or replacements

                // check for reordering
                Integer deduction;

                for (int i=0; i<newMaterialIds.length; i++) {
                    // deduct quantity based on reordering
                    // find corresponding position in prevMaterials
                    if (prevMaterials.contains(newMaterialIds[i])) {
                        for (int j=0; j<prevMaterialIds.length; j++) {
                            if(newMaterialIds[i].compareTo(prevMaterialIds[j]) == 0) {
                                deduction = newMaterialQuantities[i] - prevMaterialQuantities[j];
                                tempQuantities.add(deduction);

                            }
                        }
                    } else {
                        // else just add in newQuantity
                        tempQuantities.add(newMaterialQuantities[i]);
                    }
                }

                // determine missing ones
                for (int i=0; i<prevMaterialIds.length; i++) {
                    if (!tempMaterials.contains(prevMaterialIds[i])) { // Check if id is not in temp
                        tempMaterials.add(prevMaterialIds[i]); // Add the id to temp
                        tempQuantities.add(-prevMaterialQuantities[i]); // add back so, negative deduction
                    }
                }

                materialIds = tempMaterials.toArray(new Long[0]);
                quantityDeductions = tempQuantities.toArray(new Integer[0]);

            } else if (updatedIncidentForm.getQty() != null) {
                // only quantities updated
                // get difference then pass back
                quantityDeductions = new Integer[newMaterialQuantities.length];
                materialIds = prevMaterialIds;
                for(int i=0; i<newMaterialQuantities.length; i++) {
                    quantityDeductions[i] = newMaterialQuantities[i] - prevMaterialQuantities[i];
                }
            }
            if(quantityDeductions == null) throw new RuntimeException("quantityDeductions not determined");
            if(materialIds == null) throw new RuntimeException("materialIds not determined");

            deductQuantitytoMaterial(existingIncidentForm, quantityDeductions, materialIds, userIds);
        }



        if(updatedIncidentForm.getTime() != null) existingIncidentForm.setTime(updatedIncidentForm.getTime());
        if(updatedIncidentForm.getMaterialsInvolved() != null) existingIncidentForm.setMaterialsInvolved(updatedIncidentForm.getMaterialsInvolved());
        if(updatedIncidentForm.getInvolvedIndividuals() != null) existingIncidentForm.setInvolvedIndividuals(updatedIncidentForm.getInvolvedIndividuals());
        if(updatedIncidentForm.getNatureOfIncident() != null) existingIncidentForm.setNatureOfIncident(updatedIncidentForm.getNatureOfIncident());
        if(updatedIncidentForm.getBrand() != null) existingIncidentForm.setBrand(updatedIncidentForm.getBrand());
        if(updatedIncidentForm.getRemarks() != null) existingIncidentForm.setRemarks(updatedIncidentForm.getRemarks());


        if(updatedIncidentForm.getMaterialId() != null) existingIncidentForm.setMaterialId(updatedIncidentForm.getMaterialId());
        if(updatedIncidentForm.getQty() != null) existingIncidentForm.setQty(updatedIncidentForm.getQty());

        if (file.getSize() > 0) {
            existingIncidentForm.setFile(file.getBytes());
            existingIncidentForm.setAttachments(file.getOriginalFilename());
            existingIncidentForm.setFileType(file.getContentType());
        }

        return incidentFormRepository.save(existingIncidentForm);
    }

    public void deleteIncidentForm(Long id) {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));
        incidentFormRepository.deleteById(id);
    }
}
