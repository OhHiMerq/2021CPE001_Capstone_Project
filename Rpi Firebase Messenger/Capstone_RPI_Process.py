import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os

# Fetch the service account key JSON file contents
cred = credentials.Certificate(os.path.dirname(__file__) +"\serviceAccountKey.json")
# Initialize the app with a service account, granting admin privileges
default_app = firebase_admin.initialize_app(cred,{'databaseURL':'https://capstoneproject-4001d-default-rtdb.firebaseio.com/'})


Mess_State = '' # on or off. this value is from firebase
item_name_prev = ''
item_name = ''
item_image_link = ''

prevW = 0 # previous weight
initTime = 0
weightSend = False

def Read_Mess_State():
    # codes para basahin si Mess_State from Firebase
    ref = db.reference('CapstoneDatabase/SysMessage/')
    state = ref.get()['Mess_State']
    print('\n** Reading [Mess_State] from Firebase **')
    return state


def Image_Classification_Process_Snapshot():
    print("Taking image capture")
    item_image_link = 'image_link.com'
    return item_image_link

def Image_Classification_Process(): # returns string list, index 0 = item name , len = 3
    global item_name,item_image_link
    print("Image is being classified...")
    item_name = 'item name'
    item_image_link = Image_Classification_Process_Snapshot()
    return item_name

def Item_Weight_Process():
    print("Weighing the item... Looking for Changes")
    item_weight = 123
    return item_weight


def Main_RPI_Processes(): # incorporates conditions and running main processes
    print("\n\n############### NEW LOG #################")
    global Mess_State
            
    global item_name_prev
    item_name_curr = Image_Classification_Process()
    if item_name_prev != item_name_curr:
        item_name_prev = item_name_curr
        Mess_State = Read_Mess_State()
        if Mess_State == "on":
            # SEND DATA / get snapshot
            print("Item name Sending")
        elif Mess_State == "off":
            return


    print("-----")

    
    global prevW,initTime,weightSend
    item_weight = Item_Weight_Process()
    if item_weight != prevW and item_weight > 0:
        prevW = item_weight
        initTime = time.time()
        weightSend = False
    elif item_weight == prevW and item_weight > 0:
        if time.time() - initTime > 5 and weightSend == False:
            Mess_State = Read_Mess_State()
            if Mess_State == "on":
                print("Weight Sending")
                weightSend = True
            elif Mess_State == "off":
                return
    else:
        prevW = 0
    
# MAIN CODE
while True:
    # Mess_State Checking
    Mess_State = Read_Mess_State()
    time.sleep(2) # para may delay
    if Mess_State == 'off':
        print(' > State Value = off')
        #reset values
    elif Mess_State == 'on':
        print(' > State Value = on')
        print('** Initializing RPI Processes **')
        while True:
            Main_RPI_Processes()
            
            if Mess_State == 'off':
                print(' > State Value = off')
                print('** Terminating RPI Processes **')
                break
            time.sleep(2) # para may delay
        

    
    
        
        
    
    
