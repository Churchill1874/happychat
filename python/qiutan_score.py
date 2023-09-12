import requests
from random import choice
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
from fake_useragent import UserAgent

try:
# 从免费代理网站获取代理IP列表
    def get_proxy_list():
        url = 'https://free-proxy-list.net/'
        headers = {
    'User-Agent': UserAgent().random
}
    
        response = requests.get(url,headers=headers)
        soup = BeautifulSoup(response.text, 'html.parser')
        table = soup.find('table', class_='table table-striped table-bordered')
        rows = table.tbody.find_all('tr')
        proxies = []
        for row in rows:
            tds = row.find_all('td')
            ip = tds[0].get_text()
            port = tds[1].get_text()
            if tds[6].get_text() == 'yes':
                proxies.append(ip + ':' + port)
        return proxies


    class Match:
        def __init__(self,series,start_time,status,home_team,score,visiting_team):
            self.series = series
            self.start_time = start_time
            self.status = status
            self.home_team = home_team
            self.score = score
            self.visiting_team = visiting_team
        
        def say_hello(self):
            return str(self.__dict__)

    # 获取代理IP和User-Agent列表
    proxies = get_proxy_list()

    useragents = UserAgent()
    random_user_agent = useragents.random

    # 随机选择一个代理IP和User-Agent
    proxy = {'http': 'http://' + choice(proxies)}

    useragent = random_user_agent

    # 创建Chrome浏览器的配置项
    chrome_options = Options()
    chrome_options.add_argument('--headless')  # 启用Headless模式
    chrome_options.add_argument('--proxy-server={}'.format(proxy['http']))  # 设置代理IP
    chrome_options.add_argument('user-agent={}'.format(useragent))  # 设置User-Agent
    # 创建Chrome浏览器对象，使用上面创建的配置项
    browser = webdriver.Chrome(options=chrome_options)

    # 设置隐式等待时间为60秒
    browser.implicitly_wait(30)

    # 打开目标网页
    browser.get('https://live.titan007.com/oldIndexall.aspx')

    # 获取网页源代码
    html = browser.page_source

    # 关闭浏览器
    browser.quit()

    # 使用BeautifulSoup解析网页源代码，提取需要的数据
    soup = BeautifulSoup(html, 'html.parser')

    table_live = soup.find('table', id='table_live')
    table_live_data = table_live.tbody

    #创建list集合
    match_list = []

    # do something with soup...
    rows = table_live_data.find_all('tr')

    # 遍历每一行，打印每个单元格的内容
    for row in rows:
        # 获取行数据中的所有属性
        attrs = dict(row.attrs)
        # 判断是否包含 align="center" 这个属性
        if 'align' in attrs and attrs['align'] == 'center':       
            #如果页面上这个数据是显示的
            if 'display:none' not in row.get('style', ''):
                tds = row.find_all('td')
                if len(tds) > 0:
                    match = Match(tds[1].get_text(),tds[2].get_text(),tds[3].get_text(),tds[4].get_text(),tds[5].get_text(),tds[6].get_text())
                    match_list.append(match)
            
                #for td in tds[1:7]:
                    #print(td.get_text(), end='\t')
                #print()

    for match in match_list:
        print(match.say_hello())

except Exception as e:
    print("异常信息:",e)










