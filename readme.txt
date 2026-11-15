Alegerea pentru stergerea prieteniilor (ON DELETE CASCADE vs Service):

Am folosit constrangerea ON DELETE CASCADE direct in baza de date pe cheile straine id1 si id2 din tabela friendships (care refera users(id)). Am ales varianta asta pentru ca baza de date se ocupa automat de stergerea prieteniilor cand sterg un utilizator. Am pastrat stergerea din Service ca sa mearga aplicatia la fel cand schimb in config.properties pe MEMORY sau FILE.

Generarea ID-ului:

Creez utilizatorul doar cu nume si prenume (id-ul e null initial), iar baza de date ii genereaza id-ul automat cu GENERATED ALWAYS AS IDENTITY. In UtilizatorDbRepository la save folosesc Statement.RETURN_GENERATED_KEYS ca sa iau id-ul nou din baza de date si sa il setez pe obiect inainte sa il afisez in consola.

Toate interogarile SQL sunt facute cu PreparedStatement si parametri ? (fara concatenare de string-uri) ca sa nu mearga SQL Injection. Conexiunea si statement-urile sunt puse in try-with-resources ca sa se inchida singure.