
Steps to Compile (This is OPTIONAL, User can go to Run steps directly)
To compile the orders example:

1. Run env.bat
2. Modify the compile.bat scripts in orders\com\tibco\adapter\Orders,
orders\com\tibco\adapter\test\Orders and orders\com\acme\Orders to
fit your environment. In particular, the location of SKD and other libs.
3. cd to the top level directory (above orders\com\...).
4. Run the compileorders.bat script.

To run the example:

1. Modify the TRA files in the bin directories to fit your environment.
2. Open 2 command windows.
3. In the first window, run "OrdersAdapter.exe", This will run the adapter.
4. In the second window, run "OrdersTest.exe",This will run the adapter test program.
5. Appropriate values are generated in the test program to run tests. The test
program can operate in 3 modes:

  - Synchronous client mode (request/reply)
  - Asynchronous client mode (request/reply)
  - Publish/subscribe mode

Dhwang