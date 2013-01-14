/* * To change this template, choose Tools | Templates * and open the template in the editor. */ package mappingdocumentation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * * Записывает полученные строки в файл
 *
 * * @author dmitryrassakhatsky
 */
public
        class OutputStreaming {

    public
            String catalog;
    private
            FileOutputStream fos;

    /**
     * * Создает шапку для таблицы
     *
     * * @param newCatalog - полный путь к файлу, в который необходимо записать
     *                      * данные
     *
     * * @throws IOException * @ throws * FileNotFoundException
     */
    public
            void createTable(String newCatalog) throws IOException, FileNotFoundException {
        catalog = newCatalog;
        fos = new FileOutputStream(catalog);
        fos.write("Target message".getBytes("UTF-8"));
        fos.write(";".getBytes("UTF-8"));
        fos.write("Source message".getBytes("UTF-8"));
        fos.write(";".getBytes("UTF-8"));
        fos.write("Comments".getBytes("UTF-8"));
        fos.write("\n".getBytes("UTF-8"));
    }

    /**
     * * Создает и заполняет новые строки в файле
     *
     * * @param targetLine - Текст для 1го столбца (Целевое сообщение)
     * @param sourceLine - Текст для 2го столбца (Исходное сообщение)
     * @param comments   - Текст для 3го столбца (Коментарии)
     *
     * * @throws IOException * @ throws * FileNotFoundException
     */
    public
            void createRow(String targetLine, String sourceLine, String comments) throws IOException, FileNotFoundException {
        fos.write(targetLine.getBytes("UTF-8"));
        fos.write(";".getBytes("UTF-8"));
        fos.write(sourceLine.getBytes("UTF-8"));
        fos.write(";".getBytes("UTF-8"));
        fos.write(comments.getBytes("UTF-8"));
        fos.write("\n".getBytes("UTF-8"));
    }

    public
            void close() throws IOException, FileNotFoundException {
        fos.close();
    }
}
