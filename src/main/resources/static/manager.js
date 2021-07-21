const app = Vue.createApp({
    data() {
        return {
            clientes: [],
            firstname: "",
            lastname: "",
            email: "",
            name: "",
            maxAmount: 0,
            interest: 0,
            paymentsList: "",

        }
    },
    created() {
        axios.get("/rest/clients")
            .then(res => {
                this.clientes = res.data._embedded.clients
                console.log(this.clientes)
            })
            .catch(err => console.log(err))
    },
    methods: {
        addClient() {
            axios.post("/rest/clients", {
                    firstName: this.firstname,
                    lastName: this.lastname,
                    email: this.email,
                })
                .then(res => console.log(res))
                .catch(err => console.log(err))
        },
        deleteClient(cliente) {
            console.log(cliente)
            axios.delete(cliente._links.client.href)
            location.reload()
        },
        createTypeLoan() {
            axios.post("/api/clients/loans", "name=" + this.name + "&maxAmount=" + this.maxAmount + "&interest=" + this.interest + "&payments=" + this.paymentsList)
                .then(res => {
                    swal({
                        title: "Congratulations",
                        text: "Perfect",
                        icon: "success",
                        button: "OK",
                    })
                    location.reload();
                })
                .catch(err => console.log(err))
        }

    },
    computed: {

    }
})
app.mount("#app")

// split para string
// Splice para array