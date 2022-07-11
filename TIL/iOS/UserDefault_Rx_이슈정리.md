# UserDefault 공통 DataSource 만들기

- 공통 함수 loadString 함수를 만들다가 생긴 이슈 정리
  - loadBoolRx(key: String, defaultValue: Bool) -> Single<Bool>

    `self.UserDefaults.standard.bool(forKey: key)`는 Default 값이 false 로 정해져 있음. 하지만 활용성을 생각하면 defaultValue가 ture로 써야할 일이 있을 수도 있기 때문에 defaultValue를 넣을 수 있는 함수를 호출해서 사용하는 것이 좋다.

    UserDefaults.standard.value 는 Any? 타입을 반환하는데 Bool 로 바꿔야하기 때문에 아래와 같이 코드를 짬

    ```swift
    class UserDefaultDataSource {

        private let standard = UserDefaults.standard

    		func loadBoolRx(key: String, defaultValue: Bool) -> Single<Bool> {
    		    return Single.just(self.standard.value(forKey: key))
    		        .subscribe(on: ConcurrentDispatchQueueScheduler(qos: .background))
    		        .observe(on: ConcurrentDispatchQueueScheduler(qos: .background))
    		        .map { $0 is Bool ? $0 as! Bool : defaultValue }
    		}
    .......
    }
    ```

    `.map { $0 is Bool ? $0 as! Bool : defaultValue }` 이 코드는 처음에 `$0 == nil ? defaultValue : ($0 as! NSString) as! Bool` 로 시도해봤었는데 `($0 as! NSString).boolValue` 에서 ‘Thread 19: signal SIGABRT’ 에러가 남.

    [How To Solve SIGABRT Error in Xcode - AppyPie](https://www.appypie.com/sigabrt-xcode-swift/)

    $0 인자가 반드시 bool로 캐스팅 된다는 보장이 없기 때문에 발생하는 것 같다. 따라서 확실하게 타입캐스팅이 가능할 때 캐스팅을 해주고, 불가능할 때는 defaultValue를 넣어주는 방법을 고민했다.

    아래 코드로 테스트를 해본 후 Any에 true 를 넣어두면 옵셔널이든 아니든 Bool 타입이 맞는 것을 확인했다.

    ```swift
    var a : Any? = true //Optional(true)

    print(a is Bool?) //"true\n"
    print(a is Bool) //"true\n"

    func example() {
        var defaultValue = true //true
        let aa = [true, nil, "false"] as [Any?] //[Optional(true), nil, "false"]
        let bb = aa.map { $0 is Bool ? $0 as! Bool : defaultValue } //(4 times)
        print(bb) //"[true, true, true]\n"

        var dv = false //false
        let cc = aa.map { $0 is Bool ? $0 as! Bool : dv } //(4 times)
        print(cc) //"[true, false, false]\n"
    }

    example()
    ```

    따라서 `$0 is Bool ? $0 as! Bool : defaultValue` 를 사용했다

    - reference

    [Type Casting - The Swift Programming Language (Swift 5.6)](https://docs.swift.org/swift-book/LanguageGuide/TypeCasting.html)
