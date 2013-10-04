package org.lekbeser.eclipse.plugin.builder;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.lekbeser.eclipse.plugin.builder.BuilderOptions.BuilderOptionsBuilder;

public class BuilderDialog extends Dialog {// todo now sl: fix layout (btns)

    private final Display display;

    public BuilderDialog(Shell parent) {
        super(parent, SWT.APPLICATION_MODAL);
        display = getParent().getDisplay();
    }

    private void placeDialogInCenter(Shell shell) {
        Rectangle parentSize = getParent().getBounds();
        Rectangle mySize = shell.getBounds();
        int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
        int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
        shell.setLocation(new Point(locationX, locationY));
    }

    public void show(final BuilderOptionsBuilder optionsBuilder) throws JavaModelException {
        final BuilderOptions options = optionsBuilder.build();
        final Shell shell = createShell("Generate model builder code");
        buildHeaderPanel(shell, options);
        final List<Button> fieldButtons = buildFieldsSelectionPanel(shell, options);
        buildExtraOptionsPanel(shell, optionsBuilder);
        buildFooterCommandsPanel(shell, fieldButtons, optionsBuilder);
        shell.pack();
        placeDialogInCenter(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private Shell createShell(String title) {
        final Shell shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.CENTER);
        shell.setText(title);
        final GridLayout mainLayout = new GridLayout(2, false);
        mainLayout.marginTop = 5;
        mainLayout.marginBottom = 7;
        mainLayout.marginRight = 7;
        mainLayout.marginLeft = 7;
        mainLayout.verticalSpacing = 5;
        shell.setLayout(mainLayout);
        return shell;
    }

    private static void buildHeaderPanel(final Shell shell, final BuilderOptions options) {
        final Group gType = new Group(shell, SWT.SHADOW_ETCHED_IN);
        gType.setText("Type");
        GridData gLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gLayoutData.horizontalSpan = 2;
        gType.setLayoutData(gLayoutData);
        gType.setLayout(new RowLayout(SWT.HORIZONTAL));

        Label label = new Label(gType, SWT.NONE);
        label.setText(options.getFullTypeName());
    }

    private static void buildFooterCommandsPanel(final Shell shell, final List<Button> fieldButtons,
            final BuilderOptionsBuilder optionsBuilder) {
        final Composite group = new Composite(shell, SWT.NONE);
        GridData gLayoutData = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 2, 1);
        group.setLayoutData(gLayoutData);
        group.setLayout(new RowLayout(SWT.HORIZONTAL));

        final Button cancelButton = new Button(group, SWT.PUSH);
        cancelButton.setText("Cancel");
        final Button genButton = new Button(group, SWT.PUSH);
        genButton.setText("Generate");
        shell.setDefaultButton(genButton);

        final Listener clickListener = new Listener() {
            public void handleEvent(Event event) {
                if (event.widget == genButton) {
                    final List<IField> selectedFields = new ArrayList<IField>();
                    for (Button button : fieldButtons) {
                        if (button.getSelection()) {
                            selectedFields.add((IField) button.getData());
                        }
                    }
                    optionsBuilder.fields(selectedFields);
                    BuilderCodeGenerator.generate(optionsBuilder.build());
                    shell.dispose();
                } else {
                    shell.dispose();
                }
            }
        };
        genButton.addListener(SWT.Selection, clickListener);
        cancelButton.addListener(SWT.Selection, clickListener);
    }

    private static void buildExtraOptionsPanel(final Shell shell, final BuilderOptionsBuilder optionsBuilder) {
        final Group gOptions = new Group(shell, SWT.SHADOW_ETCHED_IN);
        gOptions.setText("Options");
        GridData gLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gLayoutData.horizontalSpan = 2;
        gLayoutData.verticalIndent = 5;
        gOptions.setLayoutData(gLayoutData);
        gOptions.setLayout(new RowLayout(SWT.VERTICAL));

        final Button cbxFormatSource = new Button(gOptions, SWT.CHECK);
        cbxFormatSource.setText("format source code (entire file) after builder code added");
        cbxFormatSource.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                optionsBuilder.formatSourceCode(cbxFormatSource.getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        final Button cbxFromMethod = new Button(gOptions, SWT.CHECK);
        cbxFromMethod.setText("add 'from' builder method");
        cbxFromMethod.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                optionsBuilder.fromMethod(cbxFromMethod.getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        final Button cbxAddWithPrefix = new Button(gOptions, SWT.CHECK);
        cbxAddWithPrefix.setText("add 'with' prefix to builder methods");
        cbxAddWithPrefix.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                optionsBuilder.addWithPrefix(cbxAddWithPrefix.getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        final Button cbxWithMethods = new Button(gOptions, SWT.CHECK);
        cbxWithMethods.setText("add 'with' methods to master bean");
        cbxWithMethods.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                optionsBuilder.addWithMethods(cbxWithMethods.getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        gOptions.pack();
    }

    private static List<Button> buildFieldsSelectionPanel(final Shell shell, final BuilderOptions options) {
        Group gFields = new Group(shell, SWT.SHADOW_ETCHED_IN);
        gFields.setText("Select fields to include");
        GridData gLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gLayoutData.horizontalSpan = 1;
        gLayoutData.verticalIndent = 5;
        gFields.setLayoutData(gLayoutData);
        gFields.setLayout(new RowLayout(SWT.VERTICAL));
        final List<Button> fieldButtons = createFieldSelectionCheckboxes(gFields, options);
        buildSelectionActionsPanel(shell, fieldButtons);
        return fieldButtons;
    }

    private static List<Button> createFieldSelectionCheckboxes(Group gFields, final BuilderOptions options) {
        final List<IField> fields = Util.findAllFields(options.getEnclosingType());
        final List<Button> fieldButtons = new ArrayList<Button>();
        for (IField field : fields) {
            Button button = new Button(gFields, SWT.CHECK);
            button.setText(Util.getName(field) + "(" + Util.getType(field) + ")");
            button.setData(field);
            button.setSelection(true);
            fieldButtons.add(button);
        }
        return fieldButtons;
    }

    private static void buildSelectionActionsPanel(final Shell shell, final List<Button> fieldButtons) {
        final Composite gActions = new Composite(shell, SWT.NONE);
        GridData gLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
        gLayoutData.horizontalSpan = 1;
        gActions.setLayoutData(gLayoutData);

        final GridLayout actionsLayout = new GridLayout(1, false);
        actionsLayout.marginTop = 10;
        actionsLayout.marginRight = 10;
        actionsLayout.marginLeft = 10;
        gActions.setLayout(actionsLayout);
        createSelectAllButton(gActions, fieldButtons);
        createSelectNoneButton(gActions, fieldButtons);
    }

    private static void createSelectAllButton(final Composite gActions, final List<Button> fieldButtons) {
        Button btn = new Button(gActions, SWT.PUSH);
        btn.setText("Select all");
        btn.addSelectionListener(new FieldSelectionAdapter(fieldButtons, true));
        GridData btnLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        btnLayoutData.horizontalSpan = 1;
        btn.setLayoutData(btnLayoutData);
    }

    private static void createSelectNoneButton(final Composite gActions, final List<Button> fieldButtons) {
        Button btn = new Button(gActions, SWT.PUSH);
        btn.setText("Deselect all");
        btn.addSelectionListener(new FieldSelectionAdapter(fieldButtons, false));
        GridData btnLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        btnLayoutData.horizontalSpan = 1;
        btn.setLayoutData(btnLayoutData);
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
