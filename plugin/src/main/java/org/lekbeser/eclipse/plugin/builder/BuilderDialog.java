package org.lekbeser.eclipse.plugin.builder;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class BuilderDialog extends Dialog {

    private final Display display;

    public BuilderDialog(Shell parent) {
        super(parent, SWT.APPLICATION_MODAL);
        display = getParent().getDisplay();
    }

    protected void display(Shell shell) {
        shell.pack();
        placeDialogInCenter(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void placeDialogInCenter(Shell shell) {
        Rectangle parentSize = getParent().getBounds();
        Rectangle mySize = shell.getBounds();

        int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
        int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;

        shell.setLocation(new Point(locationX, locationY));
    }

    public void show(final ICompilationUnit compilationUnit, final IType enclosingType) throws JavaModelException {
        final Shell shell = createShell("Generate model builder");
        createHeaderPanel(shell, enclosingType);
        final List<Button> fieldButtons = createFieldsSelectionPanel(enclosingType, shell);

        final Group optionsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        optionsGroup.setText("Options:");
        optionsGroup.setLayout(new RowLayout(SWT.VERTICAL));
        GridData optionsGridData = new GridData();
        optionsGridData.horizontalSpan = 2;
        optionsGridData.horizontalAlignment = SWT.FILL;
        optionsGroup.setLayoutData(optionsGridData);
        final Button cbxFormatSource = new Button(optionsGroup, SWT.CHECK);
        cbxFormatSource.setSelection(true);
        cbxFormatSource.setText("Format source (entire file)");

        final Button executeButton = new Button(shell, SWT.PUSH);
        executeButton.setText("Generate");
        shell.setDefaultButton(executeButton);
        final Button cancelButton = new Button(shell, SWT.PUSH);
        cancelButton.setText("Cancel");

        final Listener clickListener = new Listener() {
            public void handleEvent(Event event) {
                if (event.widget == executeButton) {

                    List<IField> selectedFields = new ArrayList<IField>();
                    for (Button button : fieldButtons) {
                        if (button.getSelection()) {
                            selectedFields.add((IField) button.getData());
                        }
                    }

                    BuilderOptions opts = BuilderOptions.builder() // --
                            .compilationUnit(compilationUnit) // --
                            .fields(selectedFields) // --
                            .enclosingType(enclosingType) // --
                            .formatSourceCode(cbxFormatSource.getSelection()) // --
                            .build();
                    BuilderCodeGenerator.generate(opts);
                    shell.dispose();
                } else {
                    shell.dispose();
                }
            }
        };

        executeButton.addListener(SWT.Selection, clickListener);
        cancelButton.addListener(SWT.Selection, clickListener);
        optionsGroup.pack();
        display(shell);
    }

    private Shell createShell(String title) {
        final Shell shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.CENTER);
        shell.setText(title);
        final GridLayout mainLayout = new GridLayout(2, false);
        mainLayout.marginTop = 10;
        mainLayout.marginBottom = 10;
        mainLayout.marginRight = 10;
        mainLayout.marginLeft = 10;
        shell.setLayout(mainLayout);
        return shell;
    }

    private static void createHeaderPanel(final Shell shell, final IType enclosingType) {
        Label label = new Label(shell, SWT.NONE);
        label.setText(enclosingType.getTypeQualifiedName());
    }

    private static List<Button> createFieldsSelectionPanel(final IType enclosingType, final Shell shell) {
        Group fieldsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        fieldsGroup.setText("Select fields to include:");
        fieldsGroup.setLayout(new RowLayout(SWT.VERTICAL));
        GridData fieldsGroupLayout = new GridData();
        fieldsGroupLayout.verticalSpan = 2;
        fieldsGroup.setLayoutData(fieldsGroupLayout);
        final List<Button> fieldButtons = createFieldSelectionCheckboxes(enclosingType, fieldsGroup);
        createSelectAllButton(shell, fieldButtons);
        createSelectNoneButton(shell, fieldButtons);
        return fieldButtons;
    }

    private static List<Button> createFieldSelectionCheckboxes(IType enclosingType, Group fieldGroup) {
        List<IField> fields = Util.findAllFields(enclosingType);
        final List<Button> fieldButtons = new ArrayList<Button>();
        for (IField field : fields) {
            Button button = new Button(fieldGroup, SWT.CHECK);
            button.setText(Util.getName(field) + "(" + Util.getType(field) + ")");
            button.setData(field);
            button.setSelection(true);
            fieldButtons.add(button);
        }
        return fieldButtons;
    }

    private static void createSelectAllButton(final Shell shell, final List<Button> fieldButtons) {
        Button btn = new Button(shell, SWT.PUSH);
        btn.setText("Select All");
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.verticalIndent = 10;
        btn.setLayoutData(data);
        btn.addSelectionListener(new FieldSelectionAdapter(fieldButtons, true));
    }

    private static void createSelectNoneButton(final Shell shell, final List<Button> fieldButtons) {
        Button btn = new Button(shell, SWT.PUSH);
        btn.setText("Deselect all");
        GridData data = new GridData();
        data.verticalAlignment = SWT.BEGINNING;
        btn.setLayoutData(data);
        btn.addSelectionListener(new FieldSelectionAdapter(fieldButtons, false));
    }

    private static class FieldSelectionAdapter extends SelectionAdapter {
        private final List<Button> buttons;
        private final boolean checked;

        public FieldSelectionAdapter(final List<Button> buttons, final boolean checked) {
            this.buttons = buttons;
            this.checked = checked;
        }

        @Override
        public void widgetSelected(SelectionEvent event) {
            for (Button button : buttons) {
                button.setSelection(checked);
            }
        }
    }

}
