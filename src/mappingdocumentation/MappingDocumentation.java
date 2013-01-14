
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

//~--- JDK imports ------------------------------------------------------------
/**
 * Импорт библиотек для работы с файлами
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author dmitryrassakhatsky
 */
public
        class MappingDocumentation {

    /**
     * Использовалась для тестирования
     *
     * @param args
     */
    public static
            void main(String[] args) throws FileNotFoundException, IOException {

        //String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/BusinessPartner__Contragents.txt";
        //String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/BusinessPartner__BusinessPartner_4_NextNumber.txt";
        //String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/BusinessPartner__BAPI_ADDRESSORG_CHANGE.txt";
        //String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/out copy.csv";
        //String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/BusinessPartner__GETINTNUMBER.txt";
        String fileCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/BusinessPartner__BAPI_ADDRESSORG_SAVEREPLICA.txt";
        String fileResultCatalog = "/Users/dmitryrassakhatsky/Desktop/Archive/out.csv";
        MappingDocumentation mappingDocumentation = new MappingDocumentation();
        mappingDocumentation.execute(fileCatalog, fileResultCatalog);
        System.out.println("Пыщь Пыщь");
    }

    /**
     * Процесс обработки файла
     *
     * @param fileCatalog       - полный путь к файлу, из которого нужно считать
     * данные данные
     * @param fileResultCatalog - полный путь к файлу, в который необходимо
     * записать данные
     *
     * @throws IOException
     */
    public
            void execute(String fileCatalog, String fileResultCatalog) throws IOException {
        /**
         * Определяем входные параметры:
         * 1) Каталог
         * 2) Входящий файл
         * 3) Исходящий файл
         */
        //String catalog = fileCatalog.substring(0, fileCatalog.lastIndexOf("/") + 1);
        FileInputStream fis = new FileInputStream(fileCatalog);
        StringBuilder preSource = new StringBuilder("");
        int a;    // Технический элемент, использующий для преобразования байтового массива в String
        /**
         * Передача байтового массива в String
         */
        while ((a = fis.read()) != -1) {
            preSource.append((char) a);
        }
        fis.close();
        Table table = new Table();
        table.createTable(preSource.toString(), fileResultCatalog);
    }
}
