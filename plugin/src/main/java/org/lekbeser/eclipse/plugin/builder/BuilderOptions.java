package org.lekbeser.eclipse.plugin.builder;

import java.util.List;
import org.eclipse.jdt.core.*;

public class BuilderOptions {

    private String typeName;
    private String fullTypeName;
    private ICompilationUnit compilationUnit;
    private IType enclosingType;
    private List<IField> fields;
    private int initialOffset;
    private boolean addWithPrefix;
    private boolean formatSourceCode;

    public BuilderOptions() {
    }

    public int getInitialOffset() {
        return initialOffset;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public ICompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public IType getEnclosingType() {
        return enclosingType;
    }

    public List<IField> getFields() {
        return fields;
    }

    public boolean isFormatSourceCode() {
        return formatSourceCode;
    }

    public boolean isAddWithPrefix() {
        return addWithPrefix;
    }

    public void setAddWithPrefix(boolean addWithPrefix) {
        this.addWithPrefix = addWithPrefix;
    }

    public static BuilderOptionsBuilder builder() {
        return new BuilderOptionsBuilder();
    }

    public static class BuilderOptionsBuilder {
        private String typeName;
        private String fullTypeName;
        private ICompilationUnit compilationUnit;
        private IType enclosingType;
        private List<IField> fields;
        private int initialOffset;
        private boolean formatSourceCode;
        private boolean addWithPrefix;

        public BuilderOptionsBuilder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public BuilderOptionsBuilder fullTypeName(String fullTypeName) {
            this.fullTypeName = fullTypeName;
            return this;
        }

        public BuilderOptionsBuilder compilationUnit(ICompilationUnit compilationUnit) {
            this.compilationUnit = compilationUnit;
            return this;
        }

        public BuilderOptionsBuilder enclosingType(IType enclosingType) {
            this.enclosingType = enclosingType;
            return this;
        }

        public BuilderOptionsBuilder fields(List<IField> fields) {
            this.fields = fields;
            return this;
        }

        public BuilderOptionsBuilder initialOffset(int initialOffset) {
            this.initialOffset = initialOffset;
            return this;
        }

        public BuilderOptionsBuilder formatSourceCode(boolean formatSourceCode) {
            this.formatSourceCode = formatSourceCode;
            return this;
        }

        public BuilderOptionsBuilder addWithPrefix(boolean is) {
            this.addWithPrefix = is;
            return this;
        }

        public BuilderOptionsBuilder from(BuilderOptions origin) {
            this.typeName(origin.typeName);
            this.fullTypeName(origin.fullTypeName);
            this.compilationUnit(origin.compilationUnit);
            this.enclosingType(origin.enclosingType);
            this.fields(origin.fields);
            this.initialOffset(origin.initialOffset);
            this.formatSourceCode(origin.formatSourceCode);
            this.addWithPrefix(origin.addWithPrefix);
            return this;
        }

        public BuilderOptions build() {
            BuilderOptions m = new BuilderOptions();
            m.typeName = this.typeName;
            m.fullTypeName = this.fullTypeName;
            m.compilationUnit = this.compilationUnit;
            m.enclosingType = this.enclosingType;
            m.fields = this.fields;
            m.initialOffset = this.initialOffset;
            m.formatSourceCode = this.formatSourceCode;
            m.addWithPrefix = this.addWithPrefix;
            return m;
        }
    }

}
