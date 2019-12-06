# Proposed Requirements

## General

### Requirements

Events can be logged to an event log file on the robot from anywhere in the robot code.

Data can be logged to a data log file on the robot periodically while the code is being run. The data to be logged, and how to
gather it, is described during initialization and does not change during a run of the robot code.

Note: There is no requirement that the event log file and the data log file be the same file.

Log files can be created where they can be downloaded via the robot's standard FTP server.

New log files are created each time the robot code is restarted.

It is possible for a user to determine which of many log files is associated with a particular match or particular time by opening
at most one human-readable text file stored alongside the log files.

If an error occurs in the logging code (including while creating or writing a log file), the error will be written to stderr once
and the robot code will continue to run with logging disabled.

Each event contains:
  * a timestamp, accurate to the millisecond
  * a severity level indicating error, warning, info, or debug
  * an arbitrary text message

Event log files can be opened, read, and understood by a non-programmer with just a text editor.

The logger can be configured to only log events at a particular severity level or higher, or to not log any events.

Data log files can be opened, read, and analyzed by a non-programmer with nothing more than widely used open source programs
capable of running on Windows, MacOS, and Linux.

Data gathering code runs on the main robot thread. This is in case the data gathering code calls code that is not thread-safe.

Data of type double can be logged.

Data that is logged is associated with a timestamp, accurate to the millisecond, corresponding to when the data was logged.

The most recently gathered data and its associated descriptions are accessible programmatically. This allows some or all of it
to be put on SmartDashboard, for example.

### Possible future features

Automatically remove old log files.

Data of type string can be logged.

Data of type int can be logged.

Track the average and max time each cycle spent doing event logging, gathering data, and writing data to the data log.

