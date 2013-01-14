/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

import java.io.IOException;

/**
 * Используется для очистки сообщений от технических структур
 *
 * @author erinalen
 */
public
        class Trash {

    /**
     * @param args the command line arguments
     */
    public static
            void main(String[] args) {
        String src = "/ns0:Messages/ns0:Message1/ns2:BAPI_ADDRESSORG_CHANGE/OBJ_ID";
        System.out.println(src);
        // System.out.println(deletePrefix(src));
    }

    /**
     * Удаляет технические ставки в сообщении
     *
     * @param source - сообщение в String, в котором необходимо удалить все
     *               технические вставки по типу "ns0:"
     *
     * @return
     */
    public
            String deletePrefix(String source) {
        String rez = ""; // результирующая переменная
        String temp = ""; // переменная для накопления букв и цифр итп
        int i = 0;
        while (i < source.length()) {
            char ch = source.charAt(i);
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
            i++;
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
    private static
            String deletePrefix2(String source) {
        String rez = ""; // результирующая переменная
        int from = 0; // с какой позиции в source ищим
        int pos_slash = 0; // позция слеша
        int pos_dvoetochie = 0; // позиция двоеточия

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

    public
            String deleteTechInformation(String target, String number) throws IOException {
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
        if (!number.equals("0")) { //Сложный маппинг
            int i1_target = target.indexOf("/", 0) + 1;
            int i2_target = target.indexOf("/", i1_target) + 1;
            int i_target = target.indexOf("/", i2_target) + 1;
            target = target.substring(i_target);
        } else {
            String tmpTarget;
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