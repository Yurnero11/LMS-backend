package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MaterialService {
    String uploadMaterial(String name, MultipartFile file, MaterialDto materialDto) throws IOException;
    List<MaterialDto> getAllMaterials();
    MaterialDto getMaterialById(Long id);
    void deleteMaterialById(Long id);
    Resource downloadMaterialByName(String fileName) throws IOException;
    boolean materialExistsByName(String name);

}
