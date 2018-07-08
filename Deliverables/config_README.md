# JAR parameterization

## JAR client
- args[0] : choice between "cli" and "gui"
- args[1] : server port (necessary for Socket connection)

If args[0] is not specified, GUI interface automatically loads.

If args[1] is not specified, default port is loaded (default port: 1337).

If port inserted is wrong, an exception is launched in a loop and program has to be reloaded.

## JAR server
- args[0] : RMI port
- args[1] : Socket port

If args[0] or args[1] is not set, both default ports will be loaded (Socket port: 1337, RMI port: 1099).

If args[0] or args[1] is wrong (and so the relative port is not available), corresponding default port is automatically loaded.
