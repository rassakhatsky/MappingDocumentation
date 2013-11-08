package mappingdocumentation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * * Записывает полученные строки в файл
 *
 * * @author dmitryrassakhatsky
 */
public class OutputStreaming {

    private FileOutputStream fos;
    private final String defaultEncoding = "UTF-8";

    /**
     * Создает шапку для таблицы
     *
     * @param newCatalog - полный путь к файлу, в который необходимо записать
     * данные
     *
     * @throws IOException * @ throws * FileNotFoundException
     * @throws java.io.FileNotFoundException
     */
    public void createTable(String newCatalog) throws IOException, FileNotFoundException {
        createTable(newCatalog, defaultEncoding);
    }

    public void createTable(String catalog, String encoding) throws IOException, FileNotFoundException {
        fos = new FileOutputStream(catalog);
        fos.write("Target message".getBytes(encoding));
        fos.write(";".getBytes(encoding));
        fos.write("Source message".getBytes(encoding));
        fos.write(";".getBytes(encoding));
        fos.write("Comments".getBytes(encoding));
        fos.write("\n".getBytes(encoding));
    }

    /**
     * * Создает и заполняет новые строки в файле
     *
     * @param targetLine - Текст для 1го столбца (Целевое сообщение)
     * @param sourceLine - Текст для 2го столбца (Исходное сообщение)
     * @param comments - Текст для 3го столбца (Коментарии)
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    public void createRow(String targetLine, String sourceLine, String comments) throws IOException, FileNotFoundException {
        createRow(targetLine, sourceLine, comments, defaultEncoding);
    }
    
    public void createRow(String targetLine, String sourceLine, String comments, String encoding) throws IOException, FileNotFoundException {
        fos.write(targetLine.getBytes(encoding));
        fos.write(";".getBytes(encoding));
        fos.write(sourceLine.getBytes(encoding));
        fos.write(";".getBytes(encoding));
        fos.write(comments.getBytes(encoding));
        fos.write("\n".getBytes(encoding));
    }

    public void close() throws IOException, FileNotFoundException {
        fos.close();
    }
}
