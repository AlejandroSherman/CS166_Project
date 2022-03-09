## How to run
How to run:

1. Login to lab machine
2. Enter /tmp folder
3. Move over the current CS166_Project folder to the /tmp folder
4. In the tmp folder make sure to have the startPostgreSQL.sh, createPostgreDB.sh, and stopPostgreDB.sh files present.

In tmp folder

5. Run source ./startPostgreSQL.sh
6. Run source ./createPostgreDB.sh
7. Enter /CS166_Project/sql/scripts/ folder

Run command:

8. cp ./*.csv $PGDATA
9. Return to tmp folder
10. Run source ./CS166_Project/sql/scripts/create_db.sh
11. Run source ./CS166_Project/java/scripts/compile.sh
12. Test the java interface
13. Type appropriate response to exit from java interface
14. Shut down server with source ./stopPostgreDB.sh
