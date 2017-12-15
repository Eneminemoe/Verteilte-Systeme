namespace java p3

typedef string String

service StoreService{

	String order(1:String message),
	String invoice(1:String message),
}