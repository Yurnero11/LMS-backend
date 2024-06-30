package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;
import com.example.yurnero.demo.repository.CourseRepository;
import com.example.yurnero.demo.repository.MaterialRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService{
    private final MaterialRepository materialRepository;
    private final CourseRepository courseRepository;
    private final String uploadDir = "C:\\Users\\Yurnero\\Desktop\\Учеба\\LMS(Learning Menegment System)\\backend\\uploads\\";

    private static final Logger logger = Logger.getLogger(MaterialServiceImpl.class.getName());


    @PostConstruct
    public void init() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                logger.log(Level.INFO, "Upload directory created successfully");
            } else {
                logger.log(Level.SEVERE, "Failed to create upload directory");
            }
        }
    }

    public MaterialServiceImpl(MaterialRepository materialRepository, CourseRepository courseRepository) {
        this.materialRepository = materialRepository;
        this.courseRepository = courseRepository;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public String uploadMaterial(String name, MultipartFile file, MaterialDto materialDto) throws IOException {
        Optional<Course> optionalCourse = courseRepository.findById(materialDto.getCourseId());

        String originalFileName = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        Material material = new Material();
        material.setName(name);
        material.setFilePath(uniqueFileName);
        material.setCourse(optionalCourse.orElse(null)); // Handle if course is not found
        materialRepository.save(material);

        String filePath = uploadDir + uniqueFileName;
        File dest = new File(filePath);
        file.transferTo(dest);

        logger.log(Level.INFO, "Material uploaded: {0}", material);
        return "Material uploaded successfully";
    }

    @Override
    public List<MaterialDto> getAllMaterials() {
        List<Material> materials = materialRepository.findAll();
        return materials.stream()
                .map(Material::getMaterialDto)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialDto getMaterialById(Long id) {
        Optional<Material> optional = materialRepository.findById(id);
        return optional.map(Material::getMaterialDto).orElse(null);
    }

    @Override
    public void deleteMaterialById(Long id) {
        materialRepository.deleteById(id);
        logger.log(Level.INFO, "Material deleted with id: {0}", id);
    }

    @Override
    public Resource downloadMaterialByName(String fileName) throws IOException {
        Optional<Material> optionalMaterial = materialRepository.findByFilePath(fileName);
        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            File file = new File(uploadDir + material.getFilePath());
            Path path = Paths.get(file.getAbsolutePath());
            Resource resource = new InputStreamResource(new FileInputStream(file));
            if (resource.exists() || resource.isReadable()) {
                logger.log(Level.INFO, "Material downloaded: {0}", material);
                return resource;
            } else {
                throw new IOException("Could not read the file: " + fileName);
            }
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    @Override
    public boolean materialExistsByName(String name) {
        return materialRepository.findByName(name).isPresent();
    }

}
