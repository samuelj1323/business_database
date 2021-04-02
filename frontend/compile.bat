javac -cp ".;lib/backend.jar" -d classes src/frontend/*.java

jar -cvfm frontend.jar MANIFEST.MF -C classes frontend
