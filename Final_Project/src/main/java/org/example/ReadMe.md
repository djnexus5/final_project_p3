In this application I present an implementation of a system to handle food and beverage items in a fridge!

The UI chart is:
        "Choose an option:"
            1. View fridge
            2. Add a grocery
                "Choose an option:"
                    1. Add some food
                     "What's the name?"
                        "What's the space required?"
                        "What's the expiry date? (please YYYY-MM-DD)"
                        "What's its category?"
                    2. Add a beverage
                        "What's the name?"
                        "What's the space required?"
                        "What's the expiry date? (please YYYY-MM-DD)"
                        "How much volume is left? (from 0 to 100%)?"
            3. Eat/Drink/Remove a grocery
                "Choose an option"
                1. Eat some food
                    "Type the name of the food to eat:"
                2. Drink a beverage
                    "Type the name of the beverage to drink:"
                3. Remove a grocery
                    "Type the name of the grocery to remove:"
                4. Remove the expired groceries
                0. Exit the program
                "Exiting program. Goodbye"
                default:
                "Invalid choice. Please try again."
        default:
                "Invalid choice. Please try again."        

The functionalities, based on the UI are:

        1. Displays the whole content of the database in which the data is stored.
        2. Adds any type of grocery to the database and asks for the necessary details afterwards.
        3. Removes a grocery item from the database or removes all the expired groceries.
********In order to use the program, the user must run it from Main.java and use the console accordingly********


The classes are:

        Main.java: The main entry point, handling the user interface and menu.
        Refrigerator.java: Manages the fridge content, including storing and removing groceries into and from the file by organizing all data with the help of a map collection.
        Grocery.java, Beverage.java, Food.java : Classes representing different grocery items.
        Storable.java and Expirable.java : Interfaces defining methods for storing and checking expiration.
        Tests.java and ConcurrentTests.java : Unit tests. 
        DBManager.java : Establishes connection with the database.

The exceptions are:
        1. DateTimeParseException: Captured during date parsing to handle invalid date format.
        2. FileNotFoundException: Managed when attempting to read from a file that doesn't exist. In this case, the program creates a new file if it's not found.
        3. IOException: Addressed when reading or writing data to a file.
        4. ClassNotFoundException: Handled when deserializing objects from a file so that the classes of the serialized objects are available during the deserialization process.
        5. RuntimeException: An exception handling mechanism to catch unexpected errors, providing a more informative error message.

The validation methods are:
        1. validateIntegerInput() to ensure an entered value is an integer.
        2. validateDoubleInput() to validate the input as a double (for volume left).
        3. validateStringInput() verifies that an entered string is not empty.
        4.validateDateInput() validates that an entered date has the YYYY-MM-DD format.

The software used for the database are MySQL Workbench and XAMPP to run the server. 

This query was used in MySQL in order to create the admin user:
GRANT ALL PRIVILEGES ON final_project.* TO 'admin'@'localhost' IDENTIFIED BY 'admin’ 

The database and the schema were created with the following queries in MySQL Workshop: CREATE DATABASE final_project; USE final_project; CREATE groceries ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, space_required INT, expiry_date DATE, volume_left DOUBLE, category VARCHAR(255));

In order to make sure that everything runs right, Unit Tests were made for each constructor. In order to address the matter of Threading, I created Concurrent Tests which runs all the tests at the same time, reducing the time necessary for the usual operation with the help of Executors import.