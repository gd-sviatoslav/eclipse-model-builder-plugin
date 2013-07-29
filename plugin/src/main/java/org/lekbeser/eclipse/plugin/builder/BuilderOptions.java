package org.lekbeser.eclipse.plugin.builder;

import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

public class BuilderOptions {

    private String typeName;
    private String fullTypeName;
    private ICompilationUnit compilationUnit;
    private IType enclosingType;
    private List<IField> fields;

    private boolean formatSourceCode;

    public BuilderOptions() {
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    public ICompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(ICompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public IType getEnclosingType() {
        return enclosingType;
    }

    public void setEnclosingType(IType enclosingType) {
        this.enclosingType = enclosingType;
    }

    public List<IField> getFields() {
        return fields;
    }

    public void setFields(List<IField> fields) {
        this.fields = fields;
    }

    public boolean isFormatSourceCode() {
        return formatSourceCode;
    }

    public void setFormatSourceCode(boolean formatSourceCode) {
        this.formatSourceCode = formatSourceCode;
    }

    public static class BuilderOptionsBuilder {

        private String typeName;
        private String fullTypeName;
        private ICompilationUnit compilationUnit;
        private IType enclosingType;
        private List<IField> fields;
        private boolean formatSourceCode;

        public BuilderOptionsBuilder typeName(String value) {
            this.typeName = value;
            return this;
        }

        public BuilderOptionsBuilder fullTypeName(String value) {
            this.fullTypeName = value;
            return this;
        }

        public BuilderOptionsBuilder compilationUnit(ICompilationUnit value) {
            this.compilationUnit = value;
            return this;
        }

        public BuilderOptionsBuilder enclosingType(IType value) {
            this.enclosingType = value;
            return this;
        }

        public BuilderOptionsBuilder fields(List<IField> value) {
            this.fields = value;
            return this;
        }

        public BuilderOptionsBuilder formatSourceCode(boolean value) {
            this.formatSourceCode = value;
            return this;
        }

        public BuilderOptions build() {
            BuilderOptions result = new BuilderOptions();
            result.setTypeName(typeName);
            result.setFullTypeName(fullTypeName);
            result.setCompilationUnit(compilationUnit);
            result.setEnclosingType(enclosingType);
            result.setFields(fields);
            result.setFormatSourceCode(formatSourceCode);
            return result;
        }
    }

    public static BuilderOptionsBuilder builder() { 
        return new BuilderOptionsBuilder(); 
    }

}
