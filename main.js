'use strict'

// Navbar 올라가면 투명하게 만들기
// event listner 등록 : 스크롤 될때마다 함수 호출
const navbar = document.querySelector('#navbar');
const navbarHeight = navbar.getBoundingClientRect().height;
document.addEventListener('scroll', () => {
    // console.log(window.scrollY); 
    // console.log(`navbarHeight: ${navbarHeight}`);

    // window scrollY가 navbarHeight보다 크면 navbar가 어두워지게
    if (window.scrollY > navbarHeight) {
        navbar.classList.add('navbar__dark');
    } else {
        navbar.classList.remove('navbar__dark');
    }
});

// navbar 메뉴 탭 할 시, 스크롤 다루기
const  navbarMenu = document.querySelector('.navbar__menu');
navbarMenu.addEventListener('click', (event) => {
    const target = event.target;
    const link = target.dataset.link;
    if (link == null){
        return;
    }
    navbarMenu.classList.remove('open');
    scrollIntoView(link);

});

// 모바일 용 화면일 때 토글 버튼 클릭 시 메뉴 나타나게하기
const navbarToggleBtn = document.querySelector('.navbar__toggle-btn');
navbarToggleBtn.addEventListener('click', () => {
   navbarMenu.classList.toggle('open') 
});


// "contact me" 클릭 시 해당 세션으로 이동하기
const homeContactBtn = document.querySelector('.home__contact');
homeContactBtn.addEventListener('click', () => {
    scrollIntoView('#contact');
});

// 윈도우에서 스크롤을 내렸을 때 home 부분이 천천히 변하게 하기
const home = document.querySelector('.home__container');
const homeHeight = home.getBoundingClientRect().height;
document.addEventListener('scroll', () => {
    home.style.opacity = 1 - window.scrollY / homeHeight;
});

// 스크롤 내렸을 때 arrow-up 나타나기
const arrowUp = document.querySelector('.arrow-up');
document.addEventListener('scroll', () =>{
    if (window.scrollY > homeHeight / 2){
        arrowUp.classList.add('visible');
    } else {
        arrowUp.classList.remove('visible');
    }
});

// arrow-up 클릭 했을 때
arrowUp.addEventListener('click', () => {
    scrollIntoView('#home');
});

// Projects
const workBtnContainer = document.querySelector('.work__categories');
const projectContainer = document.querySelector('.work__projects');
const projects = document.querySelectorAll('.project');
workBtnContainer.addEventListener('click', (e) => {
    const filter = e.target.dataset.filter || e.target.parentNode.dataset.filter;
    // console.log(filter);
    if(filter == null){
        return;
    }

    // 메뉴에서 이전 선택된 아이템에 active를 지우고, 현재 선택된 아이템에 추가
    const active = document.querySelector('.category__btn.selected');
    active.classList.remove('selected');
    const target = e.target.nodeName === 'BUTTON' ? e.target :
                    e.target.parentNode;
    target.classList.add('selected');

    projectContainer.classList.add('anim-out');
    setTimeout(() => {
        projects.forEach((project) => {
            console.log(project.dataset.type);
            if(filter === '*' || filter === project.dataset.type) {
                project.classList.remove('invisible');
            } else{
                project.classList.add('invisible');
            }
        });
        projectContainer.classList.remove('anim-out');
    }, 300);
});

function scrollIntoView(selector) {
    const scrollTo = document.querySelector(selector);
    scrollTo.scrollIntoView({ behavior: 'smooth' });
}