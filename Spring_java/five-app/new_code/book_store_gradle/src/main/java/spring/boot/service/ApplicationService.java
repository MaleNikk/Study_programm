package spring.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.configuration.AppCacheConfiguration;
import spring.boot.dto.ApplicationEntity;
import spring.boot.injector.ServiceManagement;
import spring.boot.injector.StorageManagement;

import java.util.List;

@Service
public class ApplicationService implements ServiceManagement<ApplicationEntity> {

    @Autowired
    private AppCacheConfiguration configuration;

    @Autowired
    private StorageManagement<ApplicationEntity> storageManagement;

    @Override
    public List<ApplicationEntity> viewAll(String nameTable) {
        return storageManagement.viewAll(nameTable);
    }

    @Override
    public List<ApplicationEntity> viewAllByAuthor(String nameTable, String author) {
        return storageManagement.viewAllByAuthor(nameTable,author);
    }

    @Override
    public List<ApplicationEntity> viewAllByCategory(String nameTable, Integer category_id) {
        return storageManagement.viewAllByCategory(nameTable,category_id);
    }

    @Override
    public ApplicationEntity getById(Integer id, String nameTable) {
        return storageManagement.getById(id,nameTable);
    }


    @Override
    public ApplicationEntity update(Integer id, String nameTable, ApplicationEntity updated) {
        return storageManagement.update(id,nameTable, updated);
    }

    @Override
    public ApplicationEntity save(ApplicationEntity saved, String nameTable) {
        return storageManagement.save(saved,nameTable);
    }

    @Override
    public void batchInsert(List<ApplicationEntity> applicationEntity, String nameTable) {
        storageManagement.batchInsert(applicationEntity,nameTable);
    }

    @Override
    public void remove(Integer id, String nameTable) {
        storageManagement.remove(id,nameTable);
    }
}
