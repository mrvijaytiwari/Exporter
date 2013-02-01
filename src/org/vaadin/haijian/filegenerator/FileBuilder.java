package org.vaadin.haijian.filegenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Property;

public abstract class FileBuilder {
    protected File file;
    public Container container;
    private Object[] visibleColumns;
    private Map<Object, String> columnHeaderMap;
    private String header;

    public FileBuilder(Container container) {
        this.container = container;
        columnHeaderMap = new HashMap<Object, String>();
        for (Object propertyId : container.getContainerPropertyIds()) {
            columnHeaderMap
                    .put(propertyId, propertyId.toString().toUpperCase());
        }
        if (visibleColumns == null) {
            visibleColumns = container.getContainerPropertyIds().toArray();
        }
    }

    public void setVisibleColumns(Object[] visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public File getFile() {
        try {
            resetContent();
            initTempFile();
            buildFileContent();
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    protected void initTempFile() throws IOException {
        if (file != null) {
            file.delete();
        }
        file = createTempFile();
    }

    protected void buildFileContent() {
        buildHeader();
        buildColumnHeaders();
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    protected void buildColumnHeaders() {
        if (visibleColumns.length == 0) {
            return;
        }
        onHeader();
        for (Object propertyId : visibleColumns) {
            String header = columnHeaderMap.get(propertyId);
            onNewCell();
            buildColumnHeaderCell(header);
        }
    }

    protected void onHeader() {
        onNewRow();
    }

    protected void buildColumnHeaderCell(String header) {

    }

    protected void buildHeader() {
        // TODO Auto-generated method stub

    }

    private void buildRows() {
        if (container == null || container.getItemIds().isEmpty()) {
            return;
        }
        for (Object itemId : container.getItemIds()) {
            onNewRow();
            buildRow(itemId);
        }
    }

    private void buildRow(Object itemId) {
        if (visibleColumns.length == 0) {
            return;
        }
        for (Object propertyId : visibleColumns) {
            Property<?> property = container.getContainerProperty(itemId,
                    propertyId);
            onNewCell();
            buildCell(property == null ? null : property.getValue());
        }
    }

    protected void onNewRow() {

    }

    protected void onNewCell() {

    }

    protected abstract void buildCell(Object value);

    protected void buildFooter() {
        // TODO Auto-generated method stub

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    protected File createTempFile() throws IOException {
        return File.createTempFile(getFileName(), getFileExtension());
    }

    protected abstract void writeToFile();

    public void setColumnHeader(Object propertyId, String header) {
        columnHeaderMap.put(propertyId, header);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    protected int getNumberofColumns() {
        return visibleColumns.length;
    }
}
