const app = Vue.createApp({
    data() {
        return {
            firstName: "",
            lastName: "",
            number: "",
            cvv: null,
            amount: null,
            description: "",
        }
    },
    created() {},
    methods: {
        buy() {
            axios.post("/api/clients/pay", {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    cardNumber: this.number,
                    cvv: this.cvv,
                    amount: this.amount,
                    description: this.description,
                })
                .then(res => {
                    swal({
                            title: "Congratulations",
                            text: "Buy Completed",
                            icon: "success",
                            button: "OK",
                        })
                        .then(res => {
                            location.reload()
                        })
                })
                .catch(err => {
                    swal({
                        title: "Alert",
                        text: "Check the data entered",
                        icon: "warning",
                    })
                })
        }
    },
    computed: {
        // url(index) {
        //     console.log(index)
        // }
    },
})
app.mount("#app")