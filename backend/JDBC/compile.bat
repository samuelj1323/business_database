javac -cp ".;lib/postgres-42.2.8.jar" -d classes src/backend/*.java

jar -cvfm lib/backend.jar MANIFEST.MF -C classes backend
