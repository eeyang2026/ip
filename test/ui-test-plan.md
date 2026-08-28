# Waffles UI test plan

The test runner executes one fresh Waffles session for each test case. Expected-output blocks list complete output lines that must appear in order; the startup banner and divider-only lines are intentionally omitted.

- Compile command: `javac -d _temp/test-ui-classes src/main/java/Deadline.java src/main/java/Event.java src/main/java/Task.java src/main/java/TaskStatus.java src/main/java/Todo.java src/main/java/Waffles.java`
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

## Test case: reject empty and unknown input

### Aim

Verify that an empty todo and an unknown command get friendly errors and do not become tasks.

### Inputs

```text
todo
blah
todo borrow book
list
bye
```

### Expected output

```text
Oops, a todo needs a description. Try `todo something to do`.
Oops, I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.
Got it. I've added this task:
Now you have 1 tasks in the list.
1.[T][ ] borrow book
```

## Test case: reject malformed deadlines and events

### Aim

Verify that missing descriptions, markers, and date/time values are reported without adding invalid tasks.

### Inputs

```text
deadline
deadline report /by
deadline report /by Friday
event meeting
event /from 2pm /to 4pm
event meeting /from /to 4pm
event meeting /from 2pm /to
event meeting /from 2pm /to 4pm
list
bye
```

### Expected output

```text
Oops, a deadline needs a due time after `/by`, like `deadline report /by Friday`.
Oops, a deadline needs something after `/by`.
Got it. I've added this deadline:
Now you have 1 tasks in the list.
Oops, an event needs both `/from` and `/to`, like `event meeting /from 2pm /to 4pm`.
Oops, an event needs a description before `/from`.
Oops, an event needs something after `/from`.
Oops, an event needs something after `/to`.
Got it. I've added this event:
Now you have 2 tasks in the list.
1.[D][ ] report (by: Friday)
2.[E][ ] meeting (from: 2pm | to: 4pm)
```

## Test case: reject invalid task numbers

### Aim

Verify that malformed and out-of-range mark commands do not change the task list.

### Inputs

```text
todo keep list safe
mark
mark nope
mark 2
mark 1
unmark 0
unmark 1
list
bye
```

### Expected output

```text
Oops, use mark followed by a task number, like mark 1.
Oops, that task number looks odd. Use a number, like mark 1.
Oops, that task number is out of range. Pick a number from 1 to 1.
Nice! I've marked this task as done:
  [T][X] keep list safe
Oops, that task number is out of range. Pick a number from 1 to 1.
OK, I've marked this task as not done yet:
  [T][ ] keep list safe
1.[T][ ] keep list safe
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
