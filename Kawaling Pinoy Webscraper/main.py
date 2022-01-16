# from bs4 import BeautifulSoup
# import os

# with open(os.path.dirname(__file__)+'\home.html', 'r') as html_file:
#     content = html_file.read()
    
#     soup = BeautifulSoup(content, 'lxml')
#     # courses_html_tags = soup.find_all('h5')
#     # for courses in courses_html_tags:
#     #     print(courses.text)
#     course_cards = soup.find_all('div', class_= 'card')
#     for course in course_cards:
#         course_name = course.h5.text
#         course_price = course.a.text.split()[-1]

#         print(f'{course_name} costs {course_price}')

score = [0.3,0.2,0.5,0.6,0.4]
name = ['egg','towel','pepper','sili','potato']
maxIndex = score.index(max(score))

print(f'Index: {maxIndex}, Item: {name[maxIndex]}, Score: {score[maxIndex]}')