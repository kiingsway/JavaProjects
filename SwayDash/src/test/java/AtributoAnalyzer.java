import org.example.controller.HomeController;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AtributoAnalyzer {

  public static void analisarAtributos(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      int modifiers = field.getModifiers();

      if (!Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers) && !Modifier.isPublic(modifiers)) {
        System.out.println("⚠ O atributo '" + field.getName() + "' na classe '" + clazz.getSimpleName() + "' está sem modificador (package-private).");
      }
      else if (Modifier.isPublic(modifiers)) {
        System.out.println("⚠ O atributo '" + field.getName() + "' na classe '" + clazz.getSimpleName() + "' é público! Verifique se realmente precisa ser acessível.");
      }
    }
  }

  public static void main(String[] args) {
    analisarAtributos(HomeController.class); // Substitua pela sua classe
  }
}
