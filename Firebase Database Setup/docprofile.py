import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os


class Firebase:
    def __init__(self):
        if not firebase_admin._apps:
            # Fetch the service account key JSON file contents
            cred = credentials.Certificate(os.path.dirname(__file__) +"\serviceAccountKey.json")
            # Initialize the app with a service account, granting admin privileges
            default_app = firebase_admin.initialize_app(cred,{'databaseURL':'https://capstoneproject-4001d-default-rtdb.firebaseio.com/'})

    # Add new recipe to SysRecipe
    def Add_SysRecipe(self,scrName,scrDescription,scrDefServing,scrIngredient,scrInstruction,scrTimeReq,imageURL):
        ref = db.reference('CapstoneDatabase/')
        table_ref = ref.child('SysRecipe')
        table_ref.push({
                'scrName' : scrName,
                'scrDescription' : scrDescription,
                'scrIngredient' : scrIngredient,
                'scrInstruction' : scrInstruction,
                'scrTimeReq' : scrTimeReq,
                'imageURL' : imageURL,
                'scrDefServing' : scrDefServing
        })

        #table_ref.get().

    # Add new Item to SysIngredient
    def Add_SysIngredient(self,defItemName,analogousNames,itemType,expiry,imageURL):
        ref = db.reference('CapstoneDatabase/')
        table_ref = ref.child('SysIngredient')
        table_ref.push({
                'defItemName' : defItemName,
                'analogousNames' : analogousNames,
                'expiry' : expiry,
                'imageURL' : imageURL,
                'itemType' : itemType
        })

    def Add_WebRecipe_Unit(self,unit):
        ref = db.reference('CapstoneDatabase/WebRecipeDetail/')
        table_ref = ref.child('WebRecipe_Unit')
        table_ref.push(unit)
    
    def Add_WebRecipe_Ingredient(self,item):
        ref = db.reference('CapstoneDatabase/WebRecipeDetail/')
        table_ref = ref.child('WebRecipe_Ingredient')
        table_ref.push(item)

    def Clear_Table(self,tableName):
        db.reference('CapstoneDatabase/'+tableName+'/').delete()

    def AddProfile(self,userEmail,userName):
        ref = db.reference('CapstoneDatabase/UserProfile/')
        ref.push({
                'userEmail' : userEmail,
                'userName' : userName
        })

fcon = Firebase()

fcon.AddProfile("morrisjl@gmail.com","roetreaty")
fcon.AddProfile("mcnihil@gmail.com","cheddartwenty")
fcon.AddProfile("ralamosm@gmail.com","sausagewisdom")
fcon.AddProfile("cgarcia@gmail.com","lavenderloathsome")
fcon.AddProfile("rsmartin@gmail.com","puddingwinding")
fcon.AddProfile("korectian@gmail.com","oiledthaw")
fcon.AddProfile("itlgnky@gmail.com","milkshakesnormal")
fcon.AddProfile("mngnsl@gmail.com","flaxseedthorny")
fcon.AddProfile("jamesmanaog@gmail.com","caviardaily")
fcon.AddProfile("mjewell@gmail.com","clementinechipped")
fcon.AddProfile("win.dapit@gmail.com","curryvapid")
fcon.AddProfile("maikelnai@gmail.com","gritsmongolian")
fcon.AddProfile("njpayne@gmail.com","nachostermite")



        
        


