import SwiftUI
import shared

extension User : Identifiable {}

class UserFetcher: ObservableObject {
    @Published var users = [User]()
    
    private let api = SharedApi()
    
    init() {
        load()
    }
    
    func load() {
        
        api.getUsers(completionHandler: { userResponse, error in
            guard let response = userResponse else {
                print(error?.localizedDescription ?? "An error occurred while retrieving user list")
                return
            }
            self.users = response
        })
    }
}

struct ContentView: View {
    
    @ObservedObject var fetcher = UserFetcher()
    
    var body: some View {
        VStack {
            List(fetcher.users) { user in
                Text(user.name ?? "Unknown Name")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
