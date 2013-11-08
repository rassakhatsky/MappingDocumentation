package mappingdocumentation;

import java.io.IOException;

/**
 * Используется для очистки сообщений от технических структур
 *
 * @author erinalen
 */
public class Trash {

    /**
     * Удаляет технические ставки в сообщении
     *
     * @param source - сообщение в String, в котором необходимо удалить все
     * технические вставки по типу "ns0:"
     *
     * @return
     */
    public String deletePrefix(String source) {
        String rez, temp;
        int index;

        rez = ""; // результирующая переменная
        temp = ""; // переменная для накопления букв и цифр итп
        index = 0;

        while (index < source.length()) {
            char ch = source.charAt(index);
            // если символ или цифра
            if (ch != ':' && ch != '/') {
                // накпливаем в темповую переменную
                temp += new String(Character.toString(ch));
            } // если дошли до двоеточия, значит в темповой переменной префикс
            else if (ch == ':') {
                temp = ""; // отчищаем темповую переменную
            } // если дошли до слэша, значит то что накопили в буферной переменной НЕ префикс
            else if (ch == '/') {
                rez += temp + "/"; // добавляем к резултату
                temp = ""; // отчищаем темповую переменную
            }
            index++;
        }
        rez += temp;

        return rez;
    }

    /**
     * Работает аналогично deletePrefix, просто другой способ написания
     *
     * @param source
     *
     * @return
     */
    private static String deletePrefix2(String source) {
        String rez;
        int from, pos_slash, pos_dvoetochie;

        rez = ""; // результирующая переменная
        from = 0; // с какой позиции в source ищим
        pos_slash = 0; // позция слеша
        pos_dvoetochie = 0; // позиция двоеточия

        while (pos_slash != -1) {
            pos_slash = source.indexOf("/", from);
            pos_dvoetochie = source.indexOf(":", from);

            if (pos_dvoetochie < pos_slash && pos_dvoetochie != - 1) {
                // двоеточие идет раньше слеша, следовательно между from и  pos_dvoetochie находится префикс
                from = pos_dvoetochie + 1;
            } else {
                if (pos_slash != - 1) {
                    // между from и pos_slash находится только имя тега
                    // прибавляем к результату
                    rez += source.substring(from, pos_slash) + "/";
                    from = pos_slash + 1;
                } else {
                    // не нашли больше слешей, значит берем все до конца строки
                    rez += source.substring(from);
                }
            }
        }
        return rez;
    }

    public String deleteTechInformation(String target, String number) throws IOException {
        /*
         * Удаляет до 3го "/" вкючительно или если сообщение простое, то
         * удаляется только 1й "/"
         * Возможные варианты targetMessage
         *
         * /ns0:BAPI_VENDOR_GETINTNUMBER/ACCOUNTGROUP
         *
         * /ns0:Messages/ns0:Message1/CREMAS04/IDOC/@BEGIN
         *
         * /ns0:Messages/ns0:Message1/ns1:BAPI_ADDRESSORG_SAVEREPLICA/CONTEXT
         *
         */
        int i1_target, i2_target, i_target;
        String tmpTarget;

        if (!number.equals("0")) { //Сложный маппинг
            i1_target = target.indexOf("/", 0) + 1;
            i2_target = target.indexOf("/", i1_target) + 1;
            i_target = target.indexOf("/", i2_target) + 1;
            target = target.substring(i_target);
        } else {
            if (target.length() != 0) {
                tmpTarget = target.substring(0, 1);
                if (tmpTarget.equals("/") && !tmpTarget.equals("")) {
                    target = target.substring(1);
                }
            }
        }
        return target;
    }
}
