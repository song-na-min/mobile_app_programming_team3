# mobile_app_programming_team3
## mobile app programming team3 project
### Figma Project App Prototyping Tool
- https://www.figma.com/file/0QBnaupiFsWH3vWGz2UXpu/Mobile-App-Project?node-id=0%3A1
### grandle 추가사항
- 모듈 gradle 들어가기
- 아래 해당 코드 추가
```
plugins {
    id 'kotlin-android-extensions'
}
```


```
dependencies {
	implementation "androidx.viewpager2:viewpager2:1.0.0"
}
```
- sync 를 눌러줘서 동기화 실행 하면 정상적으로 

### font폴더 추가
- res 우클릭 - New - Directory - font 입력

### 위에 Title Bar 제거
- app -> res -> values -> styles.xml파일을 들어가기 -> 아래 code 추가
```
<item name="windowActionBar">false</item>
<item name="windowNoTitle">true</item>
```
