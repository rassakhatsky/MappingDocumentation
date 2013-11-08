package mappingdocumentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dmitryrassakhatsky
 */
public class SourceCleaner {

    /**
     * На вход подается исходная часть сообщения, функция определяет является ли
     * эта часть сложной.
     * В случаи простого сообщения, передает обработку палачу, который работает
     * с целевым сообщением
     * В обратном случаи вначале делим на составные сообщения (по 1й "(" и
     * последней ")"), затем внутри этого выражения проверяем еще раз наличие
     * функции,
     * если она есть, то делим еще раз. Если нет, то разделяем полученный кусок
     * по "," и в этих уже кусках ищем сообщения.
     *
     * @param source
     *
     * @return
     *
     * @throws IOException
     */
    public String checkFunction(String source) throws IOException {
        String startBKT, endBKT, point, preSource, afterSource, iSource;
        int nextStartBKT, nextEndBKT, nextPoint, balanseBKT;
        boolean attrInd;

        startBKT = "(";
        endBKT = ")";
        point = ",";
        nextStartBKT = source.indexOf(startBKT); //1й символ "("
        nextEndBKT = source.lastIndexOf(endBKT); //Последний символ ")"
        nextPoint = source.indexOf(point); //Следующий символ ","
        attrInd = true;
        balanseBKT = 0; //Баланс (), если равен 0, то это отдельный блок

        /*
         * Проверяем наличие блоков ()
         */
        if (nextEndBKT != -1 && nextStartBKT != -1) {
            nextStartBKT = 0;
            nextEndBKT = 0;
            int i = 0;

            /*
             * Ищем отдельный блок
             */
            while (i < source.length()) {
                char ch = source.charAt(i);
                if (ch == '(') { //Выделяем место, в котором блок начинается
                    if (nextStartBKT == 0) {
                        nextStartBKT = i;
                    }
                    balanseBKT += 1;
                }
                if (ch == ')') { //Находим место где блок кончается
                    nextEndBKT = i;
                    balanseBKT -= 1;
                }
                /*
                 * Отдельный блок был найден
                 * !Блок может содержать другие блоки, поэтому проверяем их
                 * наличие
                 */

                if (balanseBKT == 0 && nextEndBKT != 0 && nextStartBKT != 0 && nextStartBKT < nextEndBKT) {
                    iSource = source.substring(nextStartBKT + 1, nextEndBKT); //Наш блок в исходном сообщении
                    iSource = checkFunction(iSource); //Проверяем блок на наличие других блоков внутри
                    preSource = source.substring(0, nextStartBKT + 1); //Данные, которые идут до начала блока - они точно не содержат в себе другие блоки
                    preSource = executeFunction(preSource);
                    afterSource = source.substring(nextEndBKT); //Данные, идущие после блока, в этом случаи есть вероятность, что они содержат в себе другой блок
                    if (afterSource.indexOf("(") == -1 || afterSource.indexOf(")") == -1) { //Проверяем наличие (), которыми обозначается блок, если они присутствуют, то обрабатываем эту часть
                        afterSource = executeFunction(afterSource);
                    } else {
                        afterSource = checkFunction(afterSource);
                    }
                    source = preSource + iSource + afterSource;
                    nextStartBKT = 0;
                }
                i++;
            }
        } else {
            source = executeFunction(source);
        }
        return source;
    }

    private String executeFunction(String source) throws IOException {
        String target;

        /*
         * Определяем необходимо ли деление
         */
        target = "";
        if (source.indexOf(",") != -1) {
            ArrayList<String> sourceList;
            int size;

            sourceList = new ArrayList(Arrays.asList(source.split(",")));
            size = sourceList.size();
            for (int i = 0; i < size; i++) {
                sourceList.add(i, executeFunctionForOne(sourceList.get(i)));
                if (target.equals("")) {
                    target += sourceList.get(i);
                } else {
                    target += "," + sourceList.get(i);
                    System.out.println(i);
                }
            }
        } else {
            target = executeFunctionForOne(source);
        }
        return target;
    }

    private String executeFunctionForOne(String source) throws IOException {

        String target;
        /*
         * Определим необходимо ли производить очистку
         */
        if (source.indexOf("/") != -1 && source.indexOf(":") != -1) { //Очистка необходима
            String number;
            MessageNum iMessageNum;

            //Определим номер сообщения
            iMessageNum = new MessageNum();
            number = iMessageNum.getMessageNumber(source);

            Trash iTrash = new Trash();
            source = iTrash.deleteTechInformation(source, number);
            target = iTrash.deletePrefix(source);
        } else { //Очистка не требуется
            target = source;
        }
        return target;
    }
    /**
     * Вырезка из полученного сообщения всех констант
     * Мы знаем, что в имении сообщения могут использоваться только символы
     * Aa-Zz,0-9, /,_
     *
     * Из этого следует, что узнать константы можно по следующиму признаку:
     * 1) Наличие знака "=" Если он присутствует, то это точно не ссылка на
     * сообщение, поэтому от него до 1й "," или ")" можно не обрабатывать - это
     * точно будет константа
     * Для надежности
     */
}
