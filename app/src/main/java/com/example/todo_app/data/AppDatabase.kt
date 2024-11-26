import androidx.room.Database;
import androidx.room.RoomDatabase
import com.example.todo_app.data.model.CheckListDao
import com.example.todo_app.data.model.FolderDao

import com.example.todo_app.data.model.ToDoDao;
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.Folder
import com.example.todo_app.model.ToDo;

@Database(entities = [ToDo::class, CheckList::class, Folder::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun toDoDao():ToDoDao
    abstract fun checkListDao(): CheckListDao
    abstract fun folderDao(): FolderDao
}