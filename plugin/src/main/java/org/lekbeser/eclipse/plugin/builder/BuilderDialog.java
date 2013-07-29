package org.lekbeser.eclipse.plugin.builder;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.lekbeser.eclipse.plugin.builder.BuilderOptions.BuilderOptionsBuilder;

public class BuilderDialog extends Dialog {

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
        buildOptionsPanel(shell, optionsBuilder);
        buildActionsPanel(shell, fieldButtons, optionsBuilder);
        shell.pack();
        placeDialogInCenter(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private Shell createShell(String title) {// todo now sl: fix layout (btns)
        final Shell shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.CENTER);
        shell.setText(title);
        final GridLayout mainLayout = new GridLayout(3, false);
        mainLayout.marginTop = 5;
        mainLayout.marginBottom = 10;
        mainLayout.marginRight = 7;
        mainLayout.marginLeft = 7;
        shell.setLayout(mainLayout);
        return shell;
    }

    private static void buildActionsPanel(final Shell shell, final List<Button> fieldButtons, final BuilderOptionsBuilder optionsBuilder) {
        final Group actionsGroup = new Group(shell, SWT.SHADOW_NONE);
        actionsGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
        GridData actionsGridData = new GridData();
        actionsGridData.horizontalSpan = 3;
        actionsGridData.horizontalAlignment = SWT.RIGHT;
        actionsGroup.setLayoutData(actionsGridData);

        final Button genButton = new Button(actionsGroup, SWT.PUSH);
        genButton.setText("Generate");
        shell.setDefaultButton(genButton);
        final Button cancelButton = new Button(actionsGroup, SWT.PUSH);
        cancelButton.setText("Cancel");

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

    private static void buildOptionsPanel(final Shell shell, final BuilderOptionsBuilder optionsBuilder) {
        final Group optionsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        optionsGroup.setText("Options");
        optionsGroup.setLayout(new RowLayout(SWT.VERTICAL));
        GridData optionsGridData = new GridData();
        optionsGridData.horizontalSpan = 3;
        optionsGridData.horizontalAlignment = SWT.FILL;
        optionsGroup.setLayoutData(optionsGridData);
        final Button cbxFormatSource = new Button(optionsGroup, SWT.CHECK);
        cbxFormatSource.setText("Format source code (entire file)");
        cbxFormatSource.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                optionsBuilder.formatSourceCode(cbxFormatSource.getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        optionsGroup.pack();
    }

    private static void buildHeaderPanel(final Shell shell, final BuilderOptions options) {
        Label label = new Label(shell, SWT.NONE);
        label.setText("Type: " + options.getFullTypeName());
        GridData data = new GridData();
        data.horizontalSpan = 3;
        data.horizontalAlignment = SWT.FILL;
        label.setLayoutData(data);
    }

    private static List<Button> buildFieldsSelectionPanel(final Shell shell, final BuilderOptions options) {
        Group fieldsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        fieldsGroup.setText("Select fields to include");
        fieldsGroup.setLayout(new RowLayout(SWT.VERTICAL));
        GridData fieldsGroupLayout = new GridData();
        fieldsGroupLayout.horizontalSpan = 2;
        fieldsGroupLayout.verticalSpan = 2;
        fieldsGroup.setLayoutData(fieldsGroupLayout);
        final List<Button> fieldButtons = createFieldSelectionCheckboxes(fieldsGroup, options);
        createSelectAllButton(shell, fieldButtons);
        createSelectNoneButton(shell, fieldButtons);
        return fieldButtons;
    }

    private static List<Button> createFieldSelectionCheckboxes(Group fieldGroup, final BuilderOptions options) {
        final List<IField> fields = Util.findAllFields(options.getEnclosingType());
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
        btn.setText("Select all");
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.verticalIndent = 10;
        data.horizontalSpan = 1;
        data.verticalSpan = 1;
        btn.setLayoutData(data);
        btn.addSelectionListener(new FieldSelectionAdapter(fieldButtons, true));
    }

    private static void createSelectNoneButton(final Shell shell, final List<Button> fieldButtons) {
        Button btn = new Button(shell, SWT.PUSH);
        btn.setText("Deselect all");
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.verticalAlignment = SWT.BEGINNING;
        data.horizontalSpan = 1;
        data.verticalSpan = 1;
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
