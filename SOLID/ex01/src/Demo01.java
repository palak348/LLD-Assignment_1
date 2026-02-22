public class Demo01 {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        IStudentRepository db = new FakeDb();
        InputParser parser = new InputParser();
        StudentValidator validator = new StudentValidator();
        OnboardingUI ui = new OnboardingUI();
        OnboardingService svc = new OnboardingService(db, parser, validator, ui);

        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(db));
    }
}
