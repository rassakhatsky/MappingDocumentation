/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *Класс
 * @author dmitryrassakhatsky
 */
public
        class Constant {

    ArrayList arrayList1 = new ArrayList();
    ArrayList arrayList2 = new ArrayList();
    int ID = 0;
    int x = 123456789;

    public static
            void main(String[] args) throws IOException {
    }

    public
            String checkConstant(String source) throws IOException {
        String target = source;
        String preTraget = "";
        String afterTarget = "";
        int similarly = source.indexOf("const(value=");
        int constantEnd1;
        int constantEnd2;

        //Проверяем, возможно это обычная константа
        //В этом случаи действие не требуется
        if (!source.equals("")) {
            if (similarly != -1) {
                source = executed(source);
                target = source;
            }
        }
        SourceCleaner sourceCleaner = new SourceCleaner();
        source=sourceCleaner.checkFunction(source);
        if (ID != 0) {
            for (int i = 0; i < arrayList2.size(); i++) {
                String part1 = arrayList1.get(i).toString();
                String part2 = arrayList2.get(i).toString();
                source = source.replace(part2, part1);
                int d = 9;
                d = d + 1;
            }
        }
        return source;
    }

    public
            String executed(String source) throws IOException {
        ID = ID + 1;
        String target = source;
        String preTraget = "";
        String afterTarget = "";
        int similarly = source.indexOf("const(value=");
        int constantEnd1 = 0;
        int constantEnd2;

        //Проверяем, возможно это обычная константа
        //В этом случаи действие не требуется
        if (!source.equals("")) {
            if (similarly != -1) {
                if (similarly != 0) {
                    preTraget = source.substring(0, similarly); //выводим все символы
                    target = source.substring(similarly);

                    constantEnd1 = target.indexOf("))");;
                    constantEnd2 = target.indexOf("),");;
                    if (constantEnd1 != -1) {
                        if (constantEnd2 == -1 || constantEnd1 < constantEnd2) {
                            afterTarget = target.substring(constantEnd1 + 1);
                            target = target.substring(0, constantEnd1 + 1);
                            afterTarget = executed(afterTarget);
                            arrayList1.add(new String(target));
                            arrayList2.add(new String("ПыщьПыщь" + Integer.toString(ID * x)));
                            source = preTraget + arrayList2.get(ID - 2) + afterTarget;
                        }
                    }
                    if (constantEnd2 != -1) {
                        if (constantEnd1 == -1 || constantEnd1 > constantEnd2) {
                            afterTarget = target.substring(constantEnd2 + 1);
                            target = target.substring(0, constantEnd2 + 1);
                            afterTarget = executed(afterTarget);
                            arrayList1.add(new String(target));
                            arrayList2.add(new String("ПыщьПыщь" + Integer.toString(ID * x)));
                            source = preTraget + arrayList2.get(ID - 2) + afterTarget;
                        }
                    }
                }
            }
            return source;
        } else {
            return source;
        }
    }
}

/*
 *
 * if (source.indexOf("const(value=") == 0) {
 *
 *
 * target = source;
 * } else { //Случай более сложный чем простая константа
 * /**
 * В этом этапе может быть 2 случая, константы есть и констант
 * нет
 *
 * //Проверяем знак =, если он есть, то константы точно присутствуют -
 * эти константы необходимо будет вырезать и нивкоем случаи не
 * обрабатовать
 * int similarly = source.indexOf("=");
 * int similarlyEnd1 = 0;
 * int similarlyEnd2 = 0;
 * if (similarly == -1) { //Знаков = в сообщении нет, поэтому можем
 * спокойно проводить проверку
 * target = checkConstant(source);//Проводим проверку
 * } else {
 * preTraget = target.substring(0, similarly + 1); //выводим все символы
 * до знака = (Это точно не константы)
 * SourceCleaner sourceCleaner = new SourceCleaner();
 * preTraget = preTraget + sourceCleaner.checkFunction(preTraget);
 * target = source.substring(similarly + 1);
 *
 * //Определим константа ли это или значение по умолчанию
 * if (similarly == source.indexOf("const(value=") +
 * "const(value=".length() - 1) { //Константа ли это, если да, то там
 * возможны символы ( ) или ,
 * /*
 * Более сложная ситуация - оканчиваться может как на
 * )),
 * так и на ), остальные случаи обработатке не подлежат
 *
 * similarlyEnd1 = target.indexOf("))");;
 * similarlyEnd2 = target.indexOf("),");;
 *
 * if (similarlyEnd1 != -1) {
 * if (similarlyEnd2 == -1 || similarlyEnd1 < similarlyEnd2) {
 * afterTarget = target.substring(similarlyEnd1 + 1);
 * target = target.substring(0, similarlyEnd1 + 1);
 * afterTarget = checkConstant(afterTarget);
 * }
 * }
 * if (similarlyEnd2 != -1) {
 * if (similarlyEnd1 == -1 || similarlyEnd1 > similarlyEnd2) {
 * afterTarget = target.substring(similarlyEnd2 + 1);
 * target = target.substring(0, similarlyEnd2 + 1);
 * afterTarget = checkConstant(afterTarget);
 * }
 * }
 * } else {//Это не константа
 * similarlyEnd2 = target.indexOf(")");
 * afterTarget = target.substring(similarlyEnd2 + 1);
 * target = target.substring(0, similarlyEnd2 + 1);
 * afterTarget = checkConstant(afterTarget);
 * }
 * }
 * }
 * target = preTraget + target + afterTarget;
 * System.out.println(source);
 * return target; //Возвращаем результат
 * } else {
 * return source;
 * }
 * * *
 */
