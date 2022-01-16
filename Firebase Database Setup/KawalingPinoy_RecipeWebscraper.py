from bs4 import BeautifulSoup
# from selenium.webdriver.chrome import options
# from selenium.webdriver.common import service
# from webdriver_manager import driver
import DatabaseSetup
import requests
# from selenium import webdriver
# from selenium.webdriver.chrome.service import Service
# from selenium.webdriver.common.by import By
# import os

def load_recipes(page):
    page_link = 'https://www.kawalingpinoy.com/recipe-index/?fwp_paged='+page
    html_text = requests.get(page_link).text
    soup = BeautifulSoup(html_text,'lxml')

    

    recipes = soup.find_all('h2',class_ = 'entry-title')
    for recipe in recipes:
        print(f'Accessing {recipe.text} recipe from page {page}.')
        recipe_link = recipe.a['href']
        html_text = requests.get(recipe_link).text
        soup = BeautifulSoup(html_text,'lxml')

        # options = webdriver.ChromeOptions()
        # options.add_experimental_option('excludeSwitches', ['enable-logging'])
        # s = Service(os.path.dirname(__file__) +'\chromedriver.exe')
        # driver = webdriver.Chrome(service = s,options=options)
        # driver.get(recipe_link)
        # button = driver.find_element(By.XPATH,"//a[@data-system='2']")
        # button.click()
        
        

        # RECIPE NAME
        recipe_name = soup.find('h1',class_='entry-title').text
        print(f'Recipe Name: {recipe_name}')
        if recipe_name in skipLink:
            print(f'The recipe "{recipe_name}" was skipped.')
            continue

        # RECIPE COOKTIME
        recipe_cooktime = ""
        if soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-time-container wprm-recipe-total-time-container'):
            cooktime = soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-time-container wprm-recipe-total-time-container') 
            cooktime_hrs = cooktime.find('span',class_ = 'wprm-recipe-details wprm-recipe-details-hours wprm-recipe-total_time wprm-recipe-total_time-hours')
            cooktime_mins = cooktime.find('span',class_ = 'wprm-recipe-details wprm-recipe-details-minutes wprm-recipe-total_time wprm-recipe-total_time-minutes')
            if(cooktime_hrs != None):
                recipe_cooktime += cooktime_hrs.text + ' hrs '
            if(cooktime_mins != None):
                recipe_cooktime += cooktime_mins.text + ' mins'
        else:
            recipe_cooktime = "no data"
        print(f'Cooktime: {recipe_cooktime}')

        # DEFAULT SERVING
        if soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-servings-container').span.span:
            recipe_default_serving = soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-servings-container').span.span.text
        else:
            recipe_default_serving = soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-servings-container').span.text

        print(f'Default Serving: {recipe_default_serving}')
        
        # RECIPE DESCRIPTION
        # if soup.find('div',class_ = 'wprm-recipe-summary wprm-block-text-normal').span.b:
        #     recipe_description = soup.find('div',class_ = 'wprm-recipe-summary wprm-block-text-normal').span.b.text
        # else:
        #     recipe_description = soup.find('div',class_ = 'wprm-recipe-summary wprm-block-text-normal').span.strong.text
        if soup.find('div',class_ = 'entry-content single-entry-content').p.em:
            recipe_description = soup.find('div',class_ = 'entry-content single-entry-content').p.em.text
        else:
            recipe_description = soup.find('div',class_ = 'entry-content single-entry-content').p.text
        print(f'Description: {recipe_description}')

        # RECIPE IMAGE URL
        recipe_image_URL = soup.find('div',class_='entry-content single-entry-content').find('img')
        if(recipe_image_URL.get('data-lazy-src')!= None):
            recipe_image_URL = recipe_image_URL['data-lazy-src']
        elif(recipe_image_URL.get('src')!= None):
            recipe_image_URL = recipe_image_URL['src']
        else:
            recipe_image_URL = "Attribute Error"
        print(f'Image URL: {recipe_image_URL}')



        # RECIPE INGREDIENTS
        ## CHANGE TO METRIC
        
        recipe_ingredients = ""
        recipe_ingredient_group = soup.find('ul',class_='wprm-recipe-ingredients').find_all('li')
       
        for ingredient in recipe_ingredient_group:
            if ingredient.find('span',class_='wprm-recipe-ingredient-amount'):
                recipe_ingredients += ingredient.find('span',class_='wprm-recipe-ingredient-amount').text + '|'
            else:
                recipe_ingredients += '|'

            if ingredient.find('span',class_='wprm-recipe-ingredient-unit'):
                u = ingredient.find('span',class_='wprm-recipe-ingredient-unit').text
                recipe_ingredients += u + '|'
                if u != '' and u[-1] == 's':
                    u = u[:-1]

                if u not in recipe_units and u != '':
                    fcon.Add_WebRecipe_Unit(u)
                    recipe_units.append(u)
            else:
                recipe_ingredients += '|'

            if ingredient.find('span',class_='wprm-recipe-ingredient-name'):
                u = ingredient.find('span',class_='wprm-recipe-ingredient-name').text
                recipe_ingredients += u + '&'
                if u not in recipe_ingredient_names and u != '':
                    fcon.Add_WebRecipe_Ingredient(u)
                    recipe_ingredient_names.append(u)
            else:
                recipe_ingredients += '&'

        print(f'Ingredients: {recipe_ingredients}')



        # RECIPE INSTRUCTION
        recipe_instruction = ""
        recipe_instruction_group = soup.find('ul',class_='wprm-recipe-instructions').find_all('li')
        for instruction in recipe_instruction_group:
            # if instruction.div.get('span') != None:
            #     recipe_instruction += instruction.div.span.text +';'
            # else:
            #     recipe_instruction += instruction.div.text +';'
            if instruction.find('span'):
                recipe_instruction += instruction.find('span').text +';'
            elif instruction.find('div'):
                recipe_instruction += instruction.find('div').text +';'
            else:
                recipe_instruction += instruction.text +';'
        print(f'Instruction: {recipe_instruction}')


        
        fcon.Add_SysRecipe(recipe_name,recipe_description,recipe_default_serving,recipe_ingredients,recipe_instruction,recipe_cooktime,recipe_image_URL)
        print('Database Status: Added')
        print('')

print("##### SESSION STARTED #####")
# for tracking purposes
recipe_units = []
recipe_ingredient_names = []
#
skipLink = ['Ten Filipino Desserts You Should Make for Christmas','Garlic Butter Fried Frog Legs']
fcon = DatabaseSetup.Firebase()
fcon.Clear_Table('SysRecipe')
fcon.Clear_Table('WebRecipeDetail')
pageMax = 33
for page in range(pageMax):
    load_recipes(str(page + 1))
# load_recipes('1')
print("##### DONE WEBSCRAPING #####")