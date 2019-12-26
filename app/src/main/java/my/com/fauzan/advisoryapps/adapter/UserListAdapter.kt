package my.com.fauzan.advisoryapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_layout.view.*
import my.com.fauzan.advisoryapps.R
import my.com.fauzan.advisoryapps.model.Model

class UserListAdapter(private val users: ArrayList<Model.Person>, private val onClick: OnItemClickListener) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    fun updateUsers(newUsers : ArrayList<Model.Person>){
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])

        holder.itemView.ll_user.setOnClickListener {
            onClick.onItemClicked(users[position])
        }

        holder.itemView.ll_user.setOnLongClickListener {
            onClick.onItemLongClicked(users[position])
            true
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: Model.Person) {
            itemView.tv_id.text = user.id.toString()
            itemView.tv_name.text = user.name
            itemView.tv_distance.text = user.distance
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(user: Model.Person)
        fun onItemLongClicked(user: Model.Person)
    }

}