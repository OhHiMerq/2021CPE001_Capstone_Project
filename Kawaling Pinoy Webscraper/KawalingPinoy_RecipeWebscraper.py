from bs4 import BeautifulSoup
import requests
import re





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

        # RECIPE NAME
        recipe_name = soup.find('h1',class_='entry-title').text
        print(f'Recipe Name: {recipe_name}')

        # RECIPE COOKTIME
        recipe_cooktime = soup.find('div',class_ = 'wprm-recipe-block-container wprm-recipe-block-container-separate wprm-block-text-normal wprm-recipe-time-container wprm-recipe-total-time-container') 
        recipe_cooktime_hrs = recipe_cooktime.find('span',class_ = 'wprm-recipe-details wprm-recipe-details-hours wprm-recipe-total_time wprm-recipe-total_time-hours')
        recipe_cooktime_mins = recipe_cooktime.find('span',class_ = 'wprm-recipe-details wprm-recipe-details-minutes wprm-recipe-total_time wprm-recipe-total_time-minutes')
        cooktime = ""
        if(recipe_cooktime_hrs != None):
            cooktime += recipe_cooktime_hrs.text + ' hrs '
        if(recipe_cooktime_mins != None):
            cooktime += recipe_cooktime_mins.text + ' mins'
        print(f'Cooktime: {cooktime}')

        # RECIPE DESCRIPTION
        recipe_description = soup.find('div',class_ = 'entry-content single-entry-content').p.em.text
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
        recipe_ingredients = ""
        recipe_ingredient_group = soup.find('ul',class_='wprm-recipe-ingredients').find_all('li')
       
        for ingredient in recipe_ingredient_group:
            if ingredient.find('span',class_='wprm-recipe-ingredient-amount'):
                recipe_ingredients += ingredient.find('span',class_='wprm-recipe-ingredient-amount').text + '|'
            else:
                recipe_ingredients += '|'

            if ingredient.find('span',class_='wprm-recipe-ingredient-unit'):
                recipe_ingredients += ingredient.find('span',class_='wprm-recipe-ingredient-unit').text + '|'
            else:
                recipe_ingredients += '|'

            if ingredient.find('span',class_='wprm-recipe-ingredient-name'):
                recipe_ingredients += ingredient.find('span',class_='wprm-recipe-ingredient-name').text + '&'
            else:
                recipe_ingredients += '&'

        print(f'Ingredients: {recipe_ingredients}')



        # RECIPE INSTRUCTION
        recipe_instruction = ""
        recipe_instruction_group = soup.find('ul',class_='wprm-recipe-instructions').find_all('li')
        for instruction in recipe_instruction_group:
            if instruction.div.get('span') != None:
                recipe_instruction += instruction.div.span.text +';'
            else:
                recipe_instruction += instruction.div.text +';'
        print(f'Instruction: {recipe_instruction}')


        print('')



load_recipes('2')
