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


def main():
    fcon = Firebase()

    # Read All SysIngredients (Considers Analogous and DefiniteName)
    ref = db.reference('CapstoneDatabase/SysIngredient').get()
    SysIngredients = []
    SysIngredients_Summary = {}
    SysIngredients_Summary_Unconsidered = []
    for ingredient in ref:
        if ref[ingredient]['analogousNames'] != "":
            SysIngredients.append(ref[ingredient]['defItemName']+","+ref[ingredient]['analogousNames']) # comma is used as separator
        else:
            SysIngredients.append(ref[ingredient]['defItemName'])
        
        SysIngredients_Summary[ref[ingredient]['defItemName']] = [] 
        # {"Item Name":["Recipe: ###|amount unit ingredientFromRecipe","",..]}

    # print(SysIngredients_Summary)
    #Read Every Ingredients needed for each Recipe
    ref = db.reference('CapstoneDatabase/SysRecipe').get()
    print("Loading...")
    for recipe in ref: # for each recipe
        # print("From " + ref[recipe]['scrName'])
        ingrSet = ref[recipe]['scrIngredient']
        ingrSet = ingrSet.split('&')
        ingrSet = ingrSet[:-1]
        # iterate all ingredients
        
        for ingr in ingrSet:
            # ingr = 1|bar|melted butter
            item = ingr.split('|')
            # ["1","bar","melted butter"]
            recipeItemName = item[2] # ingredient recipe name,
            #recipeItemName = "melted butter"
            considered = False
            # iterate from each SysIngredients
            for sysingr in SysIngredients:
                #Unsalted Butter
                sysingr_ = sysingr.split(',') # split separator in case of having analogous names
                #["Unsalted Butter"]
                for itemName in sysingr_: # kuwit
                    # " "+itemName.lower()+" " in " " + recipeItemName.replace(","," ").lower() + " "
                    itemName_ = itemName.split(' ')
                    # itemName_ = ["Unsalted","Butter"]
                    for termWord in itemName_:
                        if termWord.lower() in recipeItemName.lower():
                            summary = SysIngredients_Summary[sysingr_[0]]
                            summary.append(ref[recipe]['scrName']+": {"+item[0] + "} {" + item[1]+ "} {"+ item[2]+"}")
                            SysIngredients_Summary[itemName] = summary
                            considered = True
                            break
                    if considered:
                        break
                if considered:
                        break
            # The code will pass here if the 
            if not considered:
                unconsideredItem = ref[recipe]['scrName'] + ": {"+item[0] + "} {" + item[1]+ "} {"+ item[2]+"}"
                SysIngredients_Summary_Unconsidered.append(unconsideredItem)

    file = open(os.path.dirname(__file__)+"\\SysIngredients_Summary.txt","w")
    for ingredientSummary in SysIngredients_Summary:
        print(ingredientSummary)
        file.write(ingredientSummary + "\n")
        for usage in SysIngredients_Summary[ingredientSummary]:
            print(usage)
            file.write(usage + "\n")
        print()
        file.write("\n####################\n")
    file.close()

    file = open(os.path.dirname(__file__)+"\\SysIngredients_Summary_Unconsidered.txt","w")
    for unknown in SysIngredients_Summary_Unconsidered:
        print(unknown)
        file.write(unknown + "\n")
        print()
        file.write("\n")
    file.close()
    

main()
