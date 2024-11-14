import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoListApp extends JFrame {
    private ArrayList<Task> tasks;
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private JTextField taskField;
    private JTextField deadlineField;
    private SimpleDateFormat dateFormat;

    public TodoListApp() {
        tasks = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        setTitle("Todo List Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        taskField = new JTextField(20);
        deadlineField = new JTextField(10);
        JButton addButton = new JButton("Add Task");

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Deadline (yyyy-MM-dd):"));
        inputPanel.add(deadlineField);
        inputPanel.add(addButton);

        // Create the table
        String[] columnNames = {"Task", "Deadline", "Completed"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove Task");
        JButton updateButton = new JButton("Update Task");
        JButton toggleButton = new JButton("Toggle Completed");

        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(toggleButton);

        // Add components to the main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });

        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleCompleted();
            }
        });
    }

    private void addTask() {
        String taskDescription = taskField.getText();
        String deadlineStr = deadlineField.getText();

        if (!taskDescription.isEmpty() && !deadlineStr.isEmpty()) {
            try {
                Date deadline = dateFormat.parse(deadlineStr);
                Task task = new Task(taskDescription, deadline);
                tasks.add(task);
                updateTable();
                taskField.setText("");
                deadlineField.setText("");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both task and deadline.");
        }
    }

    private void removeTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            tasks.remove(selectedRow);
            updateTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to remove.");
        }
    }

    private void updateTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String newTaskDescription = JOptionPane.showInputDialog(this, "Enter new task description:");
            String newDeadlineStr = JOptionPane.showInputDialog(this, "Enter new deadline (yyyy-MM-dd):");

            if (newTaskDescription != null && newDeadlineStr != null) {
                try {
                    Date newDeadline = dateFormat.parse(newDeadlineStr);
                    Task task = tasks.get(selectedRow);
                    task.setDescription(newTaskDescription);
                    task.setDeadline(newDeadline);
                    updateTable();
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to update.");
        }
    }

    private void toggleCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task task = tasks.get(selectedRow);
            task.setCompleted(!task.isCompleted());
            updateTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to toggle completion.");
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Task task : tasks) {
            Object[] rowData = {
                task.getDescription(),
                dateFormat.format(task.getDeadline()),
                task.isCompleted() ? "Yes" : "No"
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TodoListApp().setVisible(true);
            }
        });
    }
}

class Task {
    private String description;
    private Date deadline;
    private boolean completed;

    public Task(String description, Date deadline) {
        this.description = description;
        this.deadline = deadline;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
