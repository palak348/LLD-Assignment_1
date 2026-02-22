import java.util.*;

public interface IStudentRepository {
    void save(StudentRecord r);

    int count();

    List<StudentRecord> all();
}