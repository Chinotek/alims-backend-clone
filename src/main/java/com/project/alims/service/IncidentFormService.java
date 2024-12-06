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

    public IncidentForm saveIncidentForm(IncidentForm incidentForm, List<MultipartFile> files) throws IOException {
        // Handle multiple files
        if (files != null && !files.isEmpty()) {
            List<byte[]> fileDataList = new ArrayList<>();
            List<String> fileNamesList = new ArrayList<>();
            List<String> fileTypesList = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    // Store file data, name, and type
                    fileDataList.add(file.getBytes());
                    fileNamesList.add(file.getOriginalFilename());
                    fileTypesList.add(file.getContentType());
                }
            }

            // Update incidentForm with the file data
            incidentForm.setFiles(fileDataList); // You should have a List<byte[]> field in your entity
            incidentForm.setAttachments(String.join(",", fileNamesList)); // Save filenames as comma-separated string
            incidentForm.setFileType(String.join(",", fileTypesList)); // Save file types as comma-separated string
        }

        // Process the quantities and materials as before
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

        // Save incident form and update material quantities
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

    public IncidentForm updateIncidentForm(Long id, IncidentForm updatedIncidentForm, List<MultipartFile> files) throws IOException {
        // Fetch the existing incident form
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));

        String users = existingIncidentForm.getUserId();

        // Update fields that are provided in the updated form
        if(updatedIncidentForm.getDate() != null) existingIncidentForm.setDate(updatedIncidentForm.getDate());
        if(updatedIncidentForm.getUserId() != null) {
            existingIncidentForm.setUserId(updatedIncidentForm.getUserId());
            users = updatedIncidentForm.getUserId();
        }

        String[] userArray = users.split(",");
        Long[] userIds = Arrays.stream(userArray)
                .map(Long::parseLong) // Convert each string to Long
                .toArray(Long[]::new);

        // Handle material quantities and materials
        if(updatedIncidentForm.getQty() != null || updatedIncidentForm.getMaterialId() != null) {
            String previousQuantities = existingIncidentForm.getQty();
            String previousMaterials = existingIncidentForm.getMaterialId();
            String newQuantities = updatedIncidentForm.getQty() != null ? updatedIncidentForm.getQty() : previousQuantities;
            String newMaterials = updatedIncidentForm.getMaterialId() != null ? updatedIncidentForm.getMaterialId() : previousMaterials;

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
                // Process material quantity changes
                ArrayList<Long> tempMaterials = new ArrayList<>(Arrays.asList(newMaterialIds));
                ArrayList<Long> prevMaterials = new ArrayList<>(Arrays.asList(prevMaterialIds));
                ArrayList<Integer> tempQuantities = new ArrayList<>();

                for (int i = 0; i < newMaterialIds.length; i++) {
                    if (prevMaterials.contains(newMaterialIds[i])) {
                        for (int j = 0; j < prevMaterialIds.length; j++) {
                            if (newMaterialIds[i].compareTo(prevMaterialIds[j]) == 0) {
                                int deduction = newMaterialQuantities[i] - prevMaterialQuantities[j];
                                tempQuantities.add(deduction);
                            }
                        }
                    } else {
                        tempQuantities.add(newMaterialQuantities[i]);
                    }
                }

                for (int i = 0; i < prevMaterialIds.length; i++) {
                    if (!tempMaterials.contains(prevMaterialIds[i])) {
                        tempMaterials.add(prevMaterialIds[i]);
                        tempQuantities.add(-prevMaterialQuantities[i]);
                    }
                }

                materialIds = tempMaterials.toArray(new Long[0]);
                quantityDeductions = tempQuantities.toArray(new Integer[0]);
            } else if (updatedIncidentForm.getQty() != null) {
                quantityDeductions = new Integer[newMaterialQuantities.length];
                materialIds = prevMaterialIds;
                for (int i = 0; i < newMaterialQuantities.length; i++) {
                    quantityDeductions[i] = newMaterialQuantities[i] - prevMaterialQuantities[i];
                }
            }

            if (quantityDeductions == null || materialIds == null) {
                throw new RuntimeException("quantityDeductions or materialIds not determined");
            }

            // Deduct quantities from materials
            deductQuantitytoMaterial(existingIncidentForm, quantityDeductions, materialIds, userIds);
        }

        // Update other fields that are provided
        if(updatedIncidentForm.getTime() != null) existingIncidentForm.setTime(updatedIncidentForm.getTime());
        if(updatedIncidentForm.getMaterialsInvolved() != null) existingIncidentForm.setMaterialsInvolved(updatedIncidentForm.getMaterialsInvolved());
        if(updatedIncidentForm.getInvolvedIndividuals() != null) existingIncidentForm.setInvolvedIndividuals(updatedIncidentForm.getInvolvedIndividuals());
        if(updatedIncidentForm.getNatureOfIncident() != null) existingIncidentForm.setNatureOfIncident(updatedIncidentForm.getNatureOfIncident());
        if(updatedIncidentForm.getBrand() != null) existingIncidentForm.setBrand(updatedIncidentForm.getBrand());
        if(updatedIncidentForm.getRemarks() != null) existingIncidentForm.setRemarks(updatedIncidentForm.getRemarks());

        if(updatedIncidentForm.getMaterialId() != null) existingIncidentForm.setMaterialId(updatedIncidentForm.getMaterialId());
        if(updatedIncidentForm.getQty() != null) existingIncidentForm.setQty(updatedIncidentForm.getQty());

        // Handle file uploads (multiple files)
        if (files != null && !files.isEmpty()) {
            List<byte[]> fileDataList = new ArrayList<>();
            List<String> fileNamesList = new ArrayList<>();
            List<String> fileTypesList = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    fileDataList.add(file.getBytes());
                    fileNamesList.add(file.getOriginalFilename());
                    fileTypesList.add(file.getContentType());
                }
            }

            // Set the file data on the incident form
            existingIncidentForm.setFiles(fileDataList); // Store multiple files as byte arrays
            existingIncidentForm.setAttachments(String.join(",", fileNamesList)); // Store filenames as a comma-separated string
            existingIncidentForm.setFileType(String.join(",", fileTypesList)); // Store file types as a comma-separated string
        }

        // Save the updated incident form
        return incidentFormRepository.save(existingIncidentForm);
    }

    public void deleteIncidentForm(Long id) {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));
        incidentFormRepository.deleteById(id);
    }
}
