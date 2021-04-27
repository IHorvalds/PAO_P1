# Proiect 1 Programare Avansata pe Obiecte

I really really __really__ ~hate~ love Java...

## Lista de actiuni
1. Get
2. GetAll
3. Update
4. Delete
5. Insert
6. Print
7. Read

## Tipuri de obiecte
1. Queryable
2. Client
3. Medic
4. Programare
5. Store
6. Service

Pentru fiecare clasa din model (Medic, Client, Programare)

Toate modelele mostenesc Queryable. Queryable tine evidenta tuturor Store-urilor pentru clasele de Modele.
Atunci cand o clasa Model noua este adaugata (in cod), un store va fi creat pentru ea cand va fi instantiata prima data. 
Sortarea se face dupa id-ul obiectelor de tip Queryable.

Clasa Service expune cateva functii simple: de adaugare, de afisare, de incarcare a obiectelor scrise pe disc, de scriere si citire a obiectelor.
Clasa Main evidentiaza functiile din Service.