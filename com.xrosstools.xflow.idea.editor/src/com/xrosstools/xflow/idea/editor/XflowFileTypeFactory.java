package com.xrosstools.xflow.idea.editor;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class XflowFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(XflowFileType.INSTANCE, XflowFileType.EXTENSION);
    }
}
