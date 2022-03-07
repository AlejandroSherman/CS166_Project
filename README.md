## How to run
How to run:
Login to lab machine
Enter /tmp folder
Move over the current CS166_Project folder to the /tmp folder
In the tmp folder make sure to have the startPostgreSQL.sh, createPostgreDB.sh, and stopPostgreDB.sh files present.

In tmp folder
Run source ./startPostgreSQL.sh
Run source ./createPostgreDB.sh
Enter /CS166_Project/sql/scripts/ folder
Run command:
cp ./*.csv $PGDATA
Return to tmp folder
Run source ./CS166_Project/sql/scripts/create_db.sh
Run source ./CS166_Project/java/scripts/compile.sh
Test the java interface
Type appropriate response to exit from java interface
Shut down server with source ./StopPostgreDB.sh
