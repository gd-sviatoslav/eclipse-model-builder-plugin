package org.lekbeser.eclipse.plugin.builder;

import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

public class BuilderOptions {

    private ICompilationUnit compilationUnit;
    private IType enclosingType;
    private List<IField> fields;

    private boolean formatSourceCode;
    private boolean addCopyTo;
    private boolean addCopyFrom;

    public BuilderOptions() {
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

    public boolean isAddCopyTo() {
        return addCopyTo;
    }

    public void setAddCopyTo(boolean addCopyTo) {
        this.addCopyTo = addCopyTo;
    }

    public boolean isAddCopyFrom() {
        return addCopyFrom;
    }

    public void setAddCopyFrom(boolean addCopyFrom) {
        this.addCopyFrom = addCopyFrom;
    }

    public static BuilderOptionsBuilder builder() {
        return new BuilderOptionsBuilder();
    }

    public static class BuilderOptionsBuilder {

        private ICompilationUnit compilationUnit;
        private IType enclosingType;
        private List<IField> fields;
        private boolean formatSourceCode;
        private boolean addCopyTo;
        private boolean addCopyFrom;

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

        public BuilderOptionsBuilder addCopyTo(boolean value) {
            this.addCopyTo = value;
            return this;
        }

        public BuilderOptionsBuilder addCopyFrom(boolean value) {
            this.addCopyFrom = value;
            return this;
        }

        public BuilderOptions build() {
            BuilderOptions result = new BuilderOptions();
            result.setCompilationUnit(compilationUnit);
            result.setEnclosingType(enclosingType);
            result.setFields(fields);
            result.setFormatSourceCode(formatSourceCode);
            result.setAddCopyTo(addCopyTo);
            result.setAddCopyFrom(addCopyFrom);
            return result;
        }
    }

}
