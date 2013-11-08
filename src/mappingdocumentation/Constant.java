package mappingdocumentation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс
 *
 * @author dmitryrassakhatsky
 */
public class Constant {

    private ArrayList arrayList1 = new ArrayList();
    private ArrayList arrayList2 = new ArrayList();
    private int ID = 0;
    private final int x = 123456789;

    public String checkConstant(String source) throws IOException {
        String target, preTraget, afterTarget;
        int similarly, constantEnd1, constantEnd2;
        SourceCleaner sourceCleaner;

        target = source;
        preTraget = "";
        afterTarget = "";
        similarly = source.indexOf("const(value=");

        //Проверяем, возможно это обычная константа
        //В этом случаи действие не требуется
        if (!source.equals("")) {
            if (similarly != -1) {
                source = executed(source);
                target = source;
            }
        }
        sourceCleaner = new SourceCleaner();
        source = sourceCleaner.checkFunction(source);
        if (ID != 0) {
            for (int i = 0; i < arrayList2.size(); i++) {
                String part1, part2;
                part1 = arrayList1.get(i).toString();
                part2 = arrayList2.get(i).toString();
                source = source.replace(part2, part1);
            }
        }
        return source;
    }

    public String executed(String source) throws IOException {
        String target, preTraget, afterTarget;
        int similarly, constantEnd1, constantEnd2;

        ID = ID + 1;
        similarly = source.indexOf("const(value=");

        //Проверяем, возможно это обычная константа
        //В этом случаи действие не требуется
        if (!source.equals("")) {
            if (similarly != -1) {
                if (similarly != 0) {
                    preTraget = source.substring(0, similarly); //выводим все символы
                    target = source.substring(similarly);

                    constantEnd1 = target.indexOf("))");
                    constantEnd2 = target.indexOf("),");
                    if (constantEnd1 != -1) {
                        if (constantEnd2 == -1 || constantEnd1 < constantEnd2) {
                            afterTarget = target.substring(constantEnd1 + 1);
                            target = target.substring(0, constantEnd1 + 1);
                            afterTarget = executed(afterTarget);
                            arrayList1.add(target);
                            arrayList2.add(("ПыщьПыщь" + Integer.toString(ID * x)));
                            source = preTraget + arrayList2.get(ID - 2) + afterTarget;
                        }
                    }
                    if (constantEnd2 != -1) {
                        if (constantEnd1 == -1 || constantEnd1 > constantEnd2) {
                            afterTarget = target.substring(constantEnd2 + 1);
                            target = target.substring(0, constantEnd2 + 1);
                            afterTarget = executed(afterTarget);
                            arrayList1.add(target);
                            arrayList2.add("ПыщьПыщь" + Integer.toString(ID * x));
                            source = preTraget + arrayList2.get(ID - 2) + afterTarget;
                        }
                    }
                }
            }
        }
        return source;
    }
}
