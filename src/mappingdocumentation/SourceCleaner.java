/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

import java.io.IOException;

/**
 *
 * @author dmitryrassakhatsky
 */
public
        class SourceCleaner {

    static
            String sourceMessage = "";
    static
            String source1 = "ifWithoutElse(stringEquals(mapWithDefault(/ns0:Messages/ns0:Message1/BUSINESS_PARTNER/RECORD/BPAddresses/Country/Kod, default_value=RU, result), const(value=RU)), /ns0:Messages/ns0:Message1/BUSINESS_PARTNER/RECORD/BPAddresses/Region/Code, keepss=false)";
    static
            String source3 = "/ns0:Messages/ns0:Message1/BUSINESS_PARTNER/RECORD/BPAddresses/Country/Kod, default_value=RU, result), const(value=RU)";
    boolean bool = true;
    static
            String ss = "";

    public static
            void main(String[] args) throws IOException {
        // String hhh = checkFunction(source1);
        System.out.println(source1);
        // System.out.println(hhh);
        System.out.println(ss);
    }

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
    public
            String checkFunction(String source) throws IOException {
        String startBKT = "(";
        String endBKT = ")";
        String point = ",";
        int nextStartBKT = source.indexOf(startBKT); //1й символ "("
        int nextEndBKT = source.lastIndexOf(endBKT); //Последний символ ")"
        int nextPoint = source.indexOf(point); //Следующий символ ","
        boolean attrInd = true;
        String preSource;
        String afterSource;
        String iSource;
        int balanseBKT = 0; //Баланс (), если равен 0, то это отдельный блок

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

    public
            String executeFunction(String source) throws IOException {
        /*
         * Определяем необходимо ли деление
         */
        String target = "";
        if (source.indexOf(",") != -1) {
            String[] Source;
            Source = source.split(",");

            for (int i = 0; i < Source.length; i++) {
                Source[i] = executeFunctionForOne(Source[i]);
                if (target.equals("")) {
                    target = target + Source[i];
                } else {
                    target = target + "," + Source[i];
                }
            }
        } else {
            target = executeFunctionForOne(source);
        }
        return target;
    }

    public
            String executeFunctionForOne(String source) throws IOException {

        String target = "";
        /*
         * Определим необходимо ли производить очистку
         */
        if (source.indexOf("/") != -1 && source.indexOf(":") != -1) { //Очистка необходима

            //Определим номер сообщения
            String number;
            MessageNum iMessageNum = new MessageNum();
            number = iMessageNum.getMessageNumber(source);

            Trash iTrash = new Trash();
            source = iTrash.deleteTechInformation(source, number);
            source = iTrash.deletePrefix(source);
            target = source;
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
     *
     * @param source
     *
     * @return
     *
     * @throws IOException
     */
}
