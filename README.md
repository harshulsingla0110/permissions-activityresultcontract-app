# Permissions App
 ### Intro ###
Here we check how to use ActivityResultContract to request permisison 
and check different stages of permission flow and how to handle them.
 ### App Permission Flow 
 **Case 1**
Permission is never requested by app.

**Case 2**
Permission requested once or more times but user denied it temporarily (Without checking “Never Ask Again”)

**Case 3**
Permission requested and user denied it permanently (By checking “Never Ask Again” option) in **sdk<29** or denies permission twice in **sdk>29**
Only one function can help us ***“shouldShowRequestPermissionRationale()”***, which returns boolean value either true or false on the base of these cases.

&nbsp; &nbsp;**Case returns TRUE:**
- When app requested permission at least once and user denied it temporarily.

&nbsp; &nbsp;**Case returns FALSE:**
- App never requested required permission before.
- App requested permission and user denied permission
