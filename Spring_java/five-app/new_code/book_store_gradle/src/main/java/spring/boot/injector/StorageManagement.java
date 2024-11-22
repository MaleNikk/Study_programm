package spring.boot.injector;

import java.util.List;

public interface StorageManagement<Object> {

    List<Object> viewAll(String nameTable);

    List<Object> viewAllByAuthor(String nameTable, String author);

    List<Object> viewAllByCategory(String nameTable, Integer category_id);

    Object getById(Integer id,String nameTable);

    Object update(Integer id,String nameTable, Object updated);

    Object save(Object saved,String nameTable);

    void batchInsert(List<Object> objects, String nameTable);

    void remove(Integer id,String nameTable);
}
