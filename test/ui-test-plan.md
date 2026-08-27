# Waffles UI test plan

The test runner executes one fresh Waffles session for each test case. Expected-output blocks list complete output lines that must appear in order; the startup banner and divider-only lines are intentionally omitted.

- Compile command: `javac -d _temp/test-ui-classes src/main/java/Deadline.java src/main/java/Event.java src/main/java/Task.java src/main/java/Todo.java src/main/java/Waffles.java`
- Run command: `java -cp _temp/test-ui-classes Waffles`

## Test case: add and list a todo

### Aim

Verify that a todo is stored, displayed with the `[T]` type marker, and counted.

### Inputs

```text
todo borrow book
list
bye
```

### Expected output

```text
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
```

## Test case: add a deadline

### Aim

Verify that a deadline preserves its description and `/by` value in the confirmation and list output.

### Inputs

```text
deadline return book /by Sunday
list
bye
```

### Expected output

```text
Got it. I've added this deadline:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
1.[D][ ] return book (by: Sunday)
```

## Test case: add an event

### Aim

Verify that an event preserves both time values and displays the divider between them.

### Inputs

```text
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
Got it. I've added this event:
  [E][ ] project meeting (from: Mon 2pm | to: 4pm)
1.[E][ ] project meeting (from: Mon 2pm | to: 4pm)
```

## Test case: mark and unmark typed tasks

### Aim

Verify that completion status is managed through the shared `Task` behavior for a typed task.

### Inputs

```text
todo clean room
mark 1
unmark 1
list
bye
```

### Expected output

```text
Nice! I've marked this task as done:
  [T][X] clean room
OK, I've marked this task as not done yet:
  [T][ ] clean room
1.[T][ ] clean room
```
