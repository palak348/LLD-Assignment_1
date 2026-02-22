import java.util.*;

public class OnboardingService {
    private final IStudentRepository repository; // Use the interface
    private final InputParser parser;
    private final StudentValidator validator;
    private final OnboardingUI ui;

    public OnboardingService(IStudentRepository repository, InputParser parser, StudentValidator validator,
            OnboardingUI ui) {
        this.repository = repository;
        this.parser = parser;
        this.validator = validator;
        this.ui = ui;
    }

    public void registerFromRawInput(String raw) {
        System.out.println("INPUT: " + raw);

        // 1. Use the Parser
        Map<String, String> kv = parser.parse(raw);

        // 2. Use the Validator
        List<String> errors = validator.validate(kv);

        if (!errors.isEmpty()) {
            ui.reportError(errors);
            return;
        }

        // 3. Extract data for the record
        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        // 4. Use ID Util and Repository
        String id = IdUtil.nextStudentId(repository.count());
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);

        repository.save(rec);
        ui.reportSuccess(rec, repository.count());

    }
}